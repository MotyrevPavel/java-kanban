import tasks.EpicTask;
import tasks.PartEpicTask;
import tasks.SimpleTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class TaskManager {
    private final HashMap<Integer, SimpleTask> taskMap;

    public TaskManager() {
        this.taskMap = new HashMap<>();
    }

    public HashMap<Integer, SimpleTask> getAllSimpleTask(){
        HashMap<Integer, SimpleTask> simpleTaskMap = new HashMap<>();

        for (SimpleTask task : taskMap.values()) {
            if (task.getClass() == SimpleTask.class){
                simpleTaskMap.put(task.getId(), task);
            }
        }

        return simpleTaskMap;
    }

    public HashMap<Integer, EpicTask> getAllEpicTask(){
        HashMap<Integer, EpicTask> epicTaskMap = new HashMap<>();

        for (SimpleTask task : taskMap.values()) {
            if (task.getClass() == EpicTask.class){
                EpicTask epicTask = (EpicTask) task;
                epicTaskMap.put(epicTask.getId(), epicTask);
            }
        }

        return epicTaskMap;
    }

    public HashMap<Integer, PartEpicTask> getAllPartTask(){
        HashMap<Integer, PartEpicTask> partTaskMap = new HashMap<>();

        for (SimpleTask task : taskMap.values()) {
            if (task.getClass() == EpicTask.class){
                EpicTask epicTask = (EpicTask) task;
                HashMap<Integer, PartEpicTask> epicTaskPartTaskMap = epicTask.getPartTaskMap();
                for (PartEpicTask partTask : epicTaskPartTaskMap.values()) {
                    partTaskMap.put(partTask.getId(), partTask);
                }
            }
        }

        return partTaskMap;
    }

    public void removeAllSimpleTask(){
        Iterator<Map.Entry<Integer, SimpleTask>> iterator = taskMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, SimpleTask> entryTaskMap = iterator.next();
            SimpleTask simpleTask = entryTaskMap.getValue();
            if(simpleTask.getClass() == SimpleTask.class){
                iterator.remove();
            }
        }
    }

    public void removeAllEpicTask(){
        removeAllPartEpicTask();
        Iterator<Map.Entry<Integer, SimpleTask>> iterator = taskMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, SimpleTask> entryTaskMap = iterator.next();
            SimpleTask simpleTask = entryTaskMap.getValue();
            if(simpleTask.getClass() == EpicTask.class){
                iterator.remove();
            }
        }
    }

    public void removeAllPartEpicTask(){
        for (SimpleTask task : taskMap.values()) {
            if (task.getClass() == EpicTask.class){
                EpicTask epicTask = (EpicTask) task;
                epicTask.removeAllPartTaskMap();
            }
        }
    }

    public Optional<SimpleTask> getTaskById(int id){
        if (taskMap.containsKey(id)){
            return Optional.of(taskMap.get(id));
        }

        for (SimpleTask task : taskMap.values()) {
            if (task.getClass() == EpicTask.class){
                EpicTask epicTask = (EpicTask) task;

                Optional<PartEpicTask> optionalPartEpicTask = epicTask.getPartEpicTaskById(id);
                if (optionalPartEpicTask.isPresent()){
                    return Optional.of(optionalPartEpicTask.get());
                }
            }
        }

        return Optional.empty();
    }

    public void makeNewTask(SimpleTask task){
        if (task.getClass() == PartEpicTask.class){
            PartEpicTask partEpicTask = (PartEpicTask) task;
            EpicTask epicTask = partEpicTask.getLinkToConnectEpicTask();
            epicTask.putPartEpicTask(partEpicTask);
            return;
        }

        if (task.getClass() == EpicTask.class){
            EpicTask epicTask = (EpicTask) task;
            taskMap.put(epicTask.getId(), epicTask);
            return;
        }

        taskMap.put(task.getId(), task);
    }

    public void updateTask(SimpleTask task){
        makeNewTask(task);
    }

    public void removeTaskById(Integer id){
        if (taskMap.containsKey(id)){
            taskMap.remove(id);
            return;
        }

        for (SimpleTask task : taskMap.values()) {
            if (task.getClass() == EpicTask.class){
                EpicTask epicTask = (EpicTask) task;
                if (epicTask.containsPartEpicTaskKey(id)) {
                    epicTask.removePartEpicTask(id);
                    return;
                }
            }
        }
    }

    public Optional<HashMap<Integer, PartEpicTask>> getEpicPartTaskMap(EpicTask epicTask){
        if(taskMap.containsValue(epicTask)){
            return Optional.of(epicTask.getPartTaskMap());
        }else {
            return Optional.empty();
        }
    }

    public Optional<HashMap<Integer, PartEpicTask>> getEpicPartTaskMapById(int epicTaskId){
        if(taskMap.containsKey(epicTaskId)){
            EpicTask epicTask = (EpicTask) taskMap.get(epicTaskId);
            return Optional.of(epicTask.getPartTaskMap());
        }else {
            return Optional.empty();
        }
    }

    public HashMap<Integer, SimpleTask> getAllTasks(){
        return taskMap;
    }

    public void printAllTask(){
        String line = "~~~~~~~";
        for (SimpleTask value : taskMap.values()) {
            System.out.println(line.repeat(10));
            System.out.println(value);
            if (value.getClass() == EpicTask.class){
                EpicTask epicTask = (EpicTask) value;
                epicTask.printAll();
            }
            System.out.println(line.repeat(10));
        }
    }
}