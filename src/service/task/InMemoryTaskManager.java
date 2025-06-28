package service.task;

import exceptions.ScheduleConflictException;
import util.Managers;
import service.history.HistoryManager;
import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;
import util.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    //Заранее прошу прощения за этот код. Я попытался избавиться от сеттеров чтобы сделать объекты неизменяемыми.
    //Чтобы при изменении объекта создавался новый объект с новыми данными а не менялись содержание списков или поля.
    //Но у меня есть стойкое ощущение что я опять написал говнокод. Прошу дать комментарий.
    //Делал это исключительно с целью попробовать.

    protected final Map<Integer, SimpleTask> simpleTaskMap;
    protected final Map<Integer, PartEpicTask> partEpicTaskMap;
    protected final HistoryManager historyManager;
    protected Map<Integer, EpicTask> epicTaskMap;
    protected Set<SimpleTask> prioritizedTaskList;


    public InMemoryTaskManager() {
        this.simpleTaskMap = new HashMap<>();
        this.epicTaskMap = new HashMap<>();
        this.partEpicTaskMap = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        Comparator<SimpleTask> comparator = Comparator.comparing(task -> task.getStartTime().get());
        comparator = comparator.thenComparing(SimpleTask::getId);
        this.prioritizedTaskList = new TreeSet<>(comparator);
    }

    @Override
    public List<SimpleTask> getAllSimpleTask() {
        return new ArrayList<>(simpleTaskMap.values());
    }

    @Override
    public List<EpicTask> getAllEpicTask() {
        return new ArrayList<>(epicTaskMap.values());
    }

    @Override
    public List<PartEpicTask> getAllPartEpicTask() {
        return new ArrayList<>(partEpicTaskMap.values());
    }

    @Override
    public void removeAllSimpleTask() {
        simpleTaskMap.keySet().forEach(historyManager::remove);
        simpleTaskMap.values().forEach(prioritizedTaskList::remove);
        simpleTaskMap.clear();
    }

    @Override
    public void removeAllEpicTask() {
        epicTaskMap.keySet().forEach(historyManager::remove);
        partEpicTaskMap.keySet().forEach(historyManager::remove);
        partEpicTaskMap.values().forEach(prioritizedTaskList::remove);
        epicTaskMap.clear();
        partEpicTaskMap.clear();
    }

    @Override
    public void removeAllPartEpicTask() {
        partEpicTaskMap.keySet().forEach(historyManager::remove);
        partEpicTaskMap.values().forEach(prioritizedTaskList::remove);
        partEpicTaskMap.clear();
        epicTaskMap = epicTaskMap.values().stream()
                .collect(Collectors.toMap(EpicTask::getId, this::getEpicTaskWithStatusNewAndEmptySubTaskList));
    }

    @Override
    public SimpleTask getSimpleTaskById(Integer id) {
        SimpleTask task = simpleTaskMap.get(id);
        addTaskToHistoryManager(id);
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(Integer id) {
        EpicTask task = epicTaskMap.get(id);
        addTaskToHistoryManager(id);
        return task;
    }

    @Override
    public PartEpicTask getPartEpicTaskById(Integer id) {
        PartEpicTask task = partEpicTaskMap.get(id);
        addTaskToHistoryManager(id);
        return task;
    }

    @Override
    public void makeNewSimpleTask(SimpleTask simpleTask) {
        Integer taskId = simpleTask.getId();
        simpleTaskMap.put(taskId, simpleTask);
        addTaskToHistoryManager(taskId);
        checkPriority(simpleTask);
    }

    @Override
    public void makeNewEpicTask(EpicTask epicTask) {
        Integer taskId = epicTask.getId();
        epicTaskMap.put(taskId, epicTask);
        addTaskToHistoryManager(taskId);
    }

    @Override
    public void makeNewPartEpicTask(PartEpicTask partEpicTask) {
        Integer taskId = partEpicTask.getId();
        Integer idConnectEpicTask = partEpicTask.getIdConnectEpicTask();

        partEpicTaskMap.put(taskId, partEpicTask);
        this.addTaskToHistoryManager(taskId);
        checkPriority(partEpicTask);

        EpicTask epicTask = epicTaskMap.get(idConnectEpicTask);
        epicTask = addSubTaskToPartEpicTaskList(epicTask, taskId);
        epicTask = updateEpicTaskStatus(epicTask);
        epicTask = updateEpicTaskEndTime(epicTask);
        epicTaskMap.put(epicTask.getId(), epicTask);
    }

    @Override
    public void updateSimpleTask(SimpleTask simpleTask) {
        makeNewSimpleTask(simpleTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        makeNewEpicTask(epicTask);
    }

    @Override
    public void updatePartEpicTask(PartEpicTask partEpicTask) {
        makeNewPartEpicTask(partEpicTask);
    }

    @Override
    public void removeSimpleTaskById(Integer id) {
        prioritizedTaskList.remove(simpleTaskMap.get(id));
        simpleTaskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicTaskById(Integer id) {
        historyManager.remove(id);
        getEpicSubtasks(epicTaskMap.get(id)).stream()
                .map(SimpleTask::getId)
                .forEach(partEpicTaskId -> {
                    partEpicTaskMap.remove(partEpicTaskId);
                    historyManager.remove(partEpicTaskId);
                });
        epicTaskMap.remove(id);
    }

    @Override
    public void removePartEpicTaskById(Integer id) {
        PartEpicTask partEpicTask = partEpicTaskMap.get(id);
        int idConnectEpicTask = partEpicTask.getIdConnectEpicTask();

        prioritizedTaskList.remove(partEpicTask);
        partEpicTaskMap.remove(id);
        historyManager.remove(id);

        EpicTask epicTask = epicTaskMap.get(idConnectEpicTask);
        epicTask = removeSubTaskFromPartEpicTaskList(epicTask, id);
        epicTask = updateEpicTaskStatus(epicTask);
        epicTask = updateEpicTaskEndTime(epicTask);
        epicTaskMap.put(epicTask.getId(), epicTask);
    }

    @Override
    public List<PartEpicTask> getEpicSubtasks(EpicTask epicTask) {
        List<Integer> listPartTaskId = epicTask.getListPartTaskId();
        return listPartTaskId.stream().map(partEpicTaskMap::get).toList();
    }

    @Override
    public List<SimpleTask> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<SimpleTask> getPrioritizedTasks() {
        Comparator<SimpleTask> comparator = Comparator.comparing(task -> task.getStartTime().get());
        comparator = comparator.thenComparing(SimpleTask::getId);
        Set<SimpleTask> set = new TreeSet<>(comparator);
        set.addAll(prioritizedTaskList);
        return set;
    }

    public boolean validateScheduleConflict(SimpleTask firstTask, SimpleTask secondTask) {
        if (firstTask.getStartTime().isPresent()
                && secondTask.getStartTime().isPresent()
                && firstTask.getDuration().isPresent()) {
            LocalDateTime firstTaskStartTime = firstTask.getStartTime().get();
            Duration firstTaskDuration = firstTask.getDuration().get();
            LocalDateTime firstTaskEndTime = firstTaskStartTime.plusMinutes(firstTaskDuration.toMinutes());
            LocalDateTime secondTaskStartTime = secondTask.getStartTime().get();
            return firstTaskEndTime.isAfter(secondTaskStartTime);
        } else {
            throw new ScheduleConflictException("Сравнение задач без времени");
        }
    }

    private EpicTask updateEpicTaskStatus(EpicTask epicTask) {
        TaskStatus status = calculateEpicStatus(epicTask);
        return new EpicTask(epicTask.getId(),
                epicTask.getName(),
                epicTask.getDescription(),
                status,
                epicTask.getStartTime().orElse(null),
                epicTask.getDuration().orElse(null),
                epicTask.getEndTime().orElse(null),
                epicTask.getListPartTaskId());

    }

    private TaskStatus calculateEpicStatus(EpicTask epicTask) {
        List<Integer> listPartTaskId = epicTask.getListPartTaskId();
        boolean isAllCurrentStatusOfPartEpicTaskNew = true;
        boolean isAllCurrentStatusOfPartEpicTaskDone = true;

        if (listPartTaskId.isEmpty()) {
            return TaskStatus.NEW;
        }

        for (Integer id : listPartTaskId) {
            PartEpicTask partEpicTask = partEpicTaskMap.get(id);
            TaskStatus currentStatusOfPartEpicTask = partEpicTask.getStatus();
            if (currentStatusOfPartEpicTask == TaskStatus.IN_PROGRESS) {
                return TaskStatus.IN_PROGRESS;
            }
            if (currentStatusOfPartEpicTask == TaskStatus.DONE) {
                isAllCurrentStatusOfPartEpicTaskNew = false;
            }
            if (currentStatusOfPartEpicTask == TaskStatus.NEW) {
                isAllCurrentStatusOfPartEpicTaskDone = false;
            }
            if (!isAllCurrentStatusOfPartEpicTaskDone && !isAllCurrentStatusOfPartEpicTaskNew) {
                return TaskStatus.IN_PROGRESS;
            }
        }

        if (isAllCurrentStatusOfPartEpicTaskNew) {
            return TaskStatus.NEW;
        }

        return TaskStatus.DONE;
    }

    private EpicTask updateEpicTaskEndTime(EpicTask epicTask) {
        List<PartEpicTask> listSubTask = getEpicSubtasks(epicTask);

        if (listSubTask.isEmpty()) {
            return new EpicTask(
                    epicTask.getId(),
                    epicTask.getName(),
                    epicTask.getDescription(),
                    epicTask.getStatus(),
                    null,
                    null,
                    null,
                    epicTask.getListPartTaskId());
        }

        Duration epicTaskDuration = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        for (PartEpicTask subTask : listSubTask) {
            if (subTask.getStartTime().isPresent()
                    && subTask.getEndTime().isPresent()
                    && subTask.getDuration().isPresent()
                    && startTime == null) {
                startTime = subTask.getStartTime().get();
                endTime = subTask.getEndTime().get();
                epicTaskDuration = subTask.getDuration().get();
            }
            if (subTask.getStartTime().isPresent()
                    && subTask.getEndTime().isPresent()
                    && subTask.getDuration().isPresent()
                    && startTime != null) {
                if (subTask.getStartTime().get().isBefore(startTime)) {
                    startTime = subTask.getStartTime().get();
                }
                if (subTask.getEndTime().get().isAfter(endTime)) {
                    endTime = subTask.getEndTime().get();
                }
                epicTaskDuration = epicTaskDuration.plusMinutes(subTask.getDuration().get().toMinutes());
            }
        }

        return new EpicTask(epicTask.getId(),
                epicTask.getName(),
                epicTask.getDescription(),
                epicTask.getStatus(),
                startTime,
                epicTaskDuration,
                endTime,
                epicTask.getListPartTaskId());
    }

    private void addTaskToHistoryManager(Integer taskId) {
        SimpleTask task;
        try {
            if (simpleTaskMap.containsKey(taskId)) {
                task = simpleTaskMap.get(taskId);
            } else if (epicTaskMap.containsKey(taskId)) {
                task = epicTaskMap.get(taskId);
            } else {
                task = partEpicTaskMap.get(taskId);
            }
            historyManager.add(task.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private EpicTask getEpicTaskWithStatusNewAndEmptySubTaskList(EpicTask task) {
        return new EpicTask(task.getId(),
                task.getName(),
                task.getDescription(),
                TaskStatus.NEW,
                task.getStartTime().orElse(null),
                task.getDuration().orElse(null),
                task.getEndTime().orElse(null),
                new ArrayList<>());
    }

    private EpicTask addSubTaskToPartEpicTaskList(EpicTask epicTask, Integer partTaskId) {
        List<Integer> listPartTaskId = epicTask.getListPartTaskId();
        if (!listPartTaskId.contains(partTaskId)) {
            listPartTaskId.add(partTaskId);
            epicTask = new EpicTask(
                    epicTask.getId(),
                    epicTask.getName(),
                    epicTask.getDescription(),
                    epicTask.getStatus(),
                    epicTask.getStartTime().orElse(null),
                    epicTask.getDuration().orElse(null),
                    epicTask.getEndTime().orElse(null),
                    listPartTaskId);
        }
        return epicTask;
    }

    private EpicTask removeSubTaskFromPartEpicTaskList(EpicTask epicTask, Integer partTaskId) {
        List<Integer> listPartTaskId = epicTask.getListPartTaskId();
        listPartTaskId.remove(partTaskId);
        return new EpicTask(
                epicTask.getId(),
                epicTask.getName(),
                epicTask.getDescription(),
                epicTask.getStatus(),
                epicTask.getStartTime().orElse(null),
                epicTask.getDuration().orElse(null),
                epicTask.getEndTime().orElse(null),
                listPartTaskId);
    }

    protected void checkPriority(SimpleTask task) {
        if (task.getStartTime().isEmpty()) {
            return;
        }
        prioritizedTaskList.add(task);
        prioritizedTaskList.stream().reduce((first, second) -> {
            boolean hasConflict = validateScheduleConflict(first, second);
            if (hasConflict) {
                throw new ScheduleConflictException("Есть пересечения в расписании выполнения задач");
            }
            return second;
        });
    }
}