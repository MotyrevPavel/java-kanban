package service.task;

import util.Managers;
import service.history.HistoryManager;
import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;
import util.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, SimpleTask> simpleTaskMap;
    protected final Map<Integer, EpicTask> epicTaskMap;
    protected final Map<Integer, PartEpicTask> partEpicTaskMap;
    protected final HistoryManager historyManager;


    public InMemoryTaskManager() {
        this.simpleTaskMap = new HashMap<>();
        this.epicTaskMap = new HashMap<>();
        this.partEpicTaskMap = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
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
        for (Integer id : simpleTaskMap.keySet()) {
            historyManager.remove(id);
        }
        simpleTaskMap.clear();
    }

    @Override
    public void removeAllEpicTask() {
        for (Integer id : epicTaskMap.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : partEpicTaskMap.keySet()) {
            historyManager.remove(id);
        }
        epicTaskMap.clear();
        partEpicTaskMap.clear();
    }

    @Override
    public void removeAllPartEpicTask() {
        for (Integer id : partEpicTaskMap.keySet()) {
            historyManager.remove(id);
        }
        partEpicTaskMap.clear();

        for (EpicTask epicTask : epicTaskMap.values()) {
            epicTask.removeAllPartTask();
            this.updateEpicTaskStatus(epicTask);
        }
    }

    @Override
    public SimpleTask getSimpleTaskById(int id) {
        SimpleTask task = simpleTaskMap.get(id);
        try {
            historyManager.add(task.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask task = epicTaskMap.get(id);
        try {
            historyManager.add(task.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return task;
    }

    @Override
    public PartEpicTask getPartEpicTaskById(int id) {
        PartEpicTask task = partEpicTaskMap.get(id);
        try {
            historyManager.add(task.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return task;
    }

    @Override
    public void makeNewSimpleTask(SimpleTask simpleTask) {
        int taskId = simpleTask.getId();
        simpleTaskMap.put(taskId, simpleTask);
        try {
            historyManager.add(simpleTask.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void makeNewEpicTask(EpicTask epicTask) {
        int taskId = epicTask.getId();
        epicTaskMap.put(taskId, epicTask);
        try {
            historyManager.add(epicTask.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void makeNewPartEpicTask(PartEpicTask partEpicTask) {
        int taskId = partEpicTask.getId();
        int idEpicTask = partEpicTask.getIdConnectEpicTask();
        EpicTask epicTask = epicTaskMap.get(idEpicTask);

        if (!epicTask.getListPartTaskId().contains(taskId)) {
            epicTask.addPartTaskId(taskId);
        }

        partEpicTaskMap.put(taskId, partEpicTask);
        this.updateEpicTaskStatus(epicTask);
        try {
            historyManager.add(partEpicTask.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void updateSimpleTask(SimpleTask simpleTask) {
        makeNewSimpleTask(simpleTask);
        try {
            historyManager.add(simpleTask.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        makeNewEpicTask(epicTask);
        try {
            historyManager.add(epicTask.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void updatePartEpicTask(PartEpicTask partEpicTask) {
        makeNewPartEpicTask(partEpicTask);
        try {
            historyManager.add(partEpicTask.clone());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void removeSimpleTaskById(int id) {
        simpleTaskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicTaskById(int id) {
        historyManager.remove(id);
        EpicTask epicTask = epicTaskMap.get(id);
        List<PartEpicTask> listOfAllPartEpicTaskExactEpic = this.getListOfAllPartEpicTaskExactEpic(epicTask);

        for (PartEpicTask partEpicTask : listOfAllPartEpicTaskExactEpic) {
            int partEpicTaskId = partEpicTask.getId();
            partEpicTaskMap.remove(partEpicTaskId);
            historyManager.remove(partEpicTaskId);
        }

        epicTaskMap.remove(id);

    }

    @Override
    public void removePartEpicTaskById(int id) {
        PartEpicTask partEpicTask = partEpicTaskMap.get(id);
        int idConnectEpicTask = partEpicTask.getIdConnectEpicTask();
        EpicTask epicTask = epicTaskMap.get(idConnectEpicTask);
        epicTask.removePartTaskById(id);
        partEpicTaskMap.remove(id);
        historyManager.remove(id);
        this.updateEpicTaskStatus(epicTask);
    }

    @Override
    public List<PartEpicTask> getListOfAllPartEpicTaskExactEpic(EpicTask epicTask) {
        List<PartEpicTask> listOfAllPartEpicTaskExactEpic = new ArrayList<>();

        List<Integer> listPartTaskId = epicTask.getListPartTaskId();
        for (Integer id : listPartTaskId) {
            PartEpicTask partEpicTaskExactEpic = this.getPartEpicTaskById(id);
            listOfAllPartEpicTaskExactEpic.add(partEpicTaskExactEpic);
        }

        return listOfAllPartEpicTaskExactEpic;
    }

    @Override
    public List<SimpleTask> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicTaskStatus(EpicTask epicTask) {
        List<Integer> listPartTaskId = epicTask.getListPartTaskId();
        boolean isAllCurrentStatusOfPartEpicTaskNew = true;
        boolean isAllCurrentStatusOfPartEpicTaskDone = true;

        if (listPartTaskId.isEmpty()) {
            epicTask.setStatus(TaskStatus.NEW);
            return;
        }

        for (Integer id : listPartTaskId) {
            PartEpicTask partEpicTask = partEpicTaskMap.get(id);
            TaskStatus currentStatusOfPartEpicTask = partEpicTask.getStatus();
            if (currentStatusOfPartEpicTask == TaskStatus.IN_PROGRESS) {
                epicTask.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
            if (currentStatusOfPartEpicTask == TaskStatus.DONE) {
                isAllCurrentStatusOfPartEpicTaskNew = false;
            }
            if (currentStatusOfPartEpicTask == TaskStatus.NEW) {
                isAllCurrentStatusOfPartEpicTaskDone = false;
            }
            if (!isAllCurrentStatusOfPartEpicTaskDone && !isAllCurrentStatusOfPartEpicTaskNew) {
                epicTask.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }

        if (isAllCurrentStatusOfPartEpicTaskNew) {
            epicTask.setStatus(TaskStatus.NEW);
            return;
        }

        epicTask.setStatus(TaskStatus.DONE);
    }
}