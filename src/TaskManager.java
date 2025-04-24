import tasks.EpicTask;
import tasks.PartEpicTask;
import tasks.SimpleTask;
import tasks.TaskStatus;

import java.util.*;

public class TaskManager {
    private final HashMap<Integer, SimpleTask> simpleTaskMap;
    private final HashMap<Integer, EpicTask> epicTaskMap;
    private final HashMap<Integer, PartEpicTask> partEpicTaskMap;

    public TaskManager() {
        this.simpleTaskMap = new HashMap<>();
        this.epicTaskMap = new HashMap<>();
        this.partEpicTaskMap = new HashMap<>();
    }

    public ArrayList<SimpleTask> getAllSimpleTask(){
        return new ArrayList<>(simpleTaskMap.values());
    }

    public ArrayList<EpicTask> getAllEpicTask(){
        return new ArrayList<>(epicTaskMap.values());
    }

    public ArrayList<PartEpicTask> getAllPartEpicTask(){
        return new ArrayList<>(partEpicTaskMap.values());
    }

    public void removeAllSimpleTask(){
        simpleTaskMap.clear();
    }

    public void removeAllEpicTask(){
        epicTaskMap.clear();
        partEpicTaskMap.clear();
    }

    public void removeAllPartEpicTask(){
        partEpicTaskMap.clear();

        for (EpicTask epicTask : epicTaskMap.values()) {
            epicTask.removeAllPartTask();
            this.updateEpicTaskStatus(epicTask);
        }
    }

    public SimpleTask getSimpleTaskById(int id){
        return simpleTaskMap.get(id);
    }

    public EpicTask getEpicTaskById(int id){
        return epicTaskMap.get(id);
    }

    public PartEpicTask getPartEpicTaskById(int id){
        return partEpicTaskMap.get(id);
    }

    public void makeNewSimpleTask(SimpleTask simpleTask){
        int taskId = simpleTask.getId();
        simpleTaskMap.put(taskId, simpleTask);
    }

    public void makeNewEpicTask(EpicTask epicTask){
        int taskId = epicTask.getId();
        epicTaskMap.put(taskId, epicTask);
    }

    public void makeNewPartEpicTask(PartEpicTask partEpicTask){
        int taskId = partEpicTask.getId();
        int idEpicTask = partEpicTask.getIdConnectEpicTask();
        EpicTask epicTask = epicTaskMap.get(idEpicTask);

        if (!epicTask.getListPartTaskId().contains(taskId)){
            epicTask.addPartTaskId(taskId);
        }

        partEpicTaskMap.put(taskId, partEpicTask);
        this.updateEpicTaskStatus(epicTask);
    }

    public void updateSimpleTask(SimpleTask simpleTask){
        makeNewSimpleTask(simpleTask);
    }

    public void updateEpicTask(EpicTask epicTask){
        makeNewEpicTask(epicTask);
    }

    public void updatePartEpicTask(PartEpicTask partEpicTask){
        makeNewPartEpicTask(partEpicTask);
    }

    public void removeSimpleTaskById(int id){
       simpleTaskMap.remove(id);
    }

    public void removeEpicTaskById(int id){
        EpicTask epicTask = epicTaskMap.get(id);
        ArrayList<PartEpicTask> listOfAllPartEpicTaskExactEpic = this.getListOfAllPartEpicTaskExactEpic(epicTask);

        for (PartEpicTask partEpicTask : listOfAllPartEpicTaskExactEpic) {
            int partEpicTaskId = partEpicTask.getId();
            partEpicTaskMap.remove(partEpicTaskId);
        }

        epicTaskMap.remove(id);

    }

    public void removePartEpicTaskById(int id){
        PartEpicTask partEpicTask = partEpicTaskMap.get(id);
        int idConnectEpicTask = partEpicTask.getIdConnectEpicTask();
        EpicTask epicTask = epicTaskMap.get(idConnectEpicTask);
        epicTask.removePartTaskById(id);
        partEpicTaskMap.remove(id);
        this.updateEpicTaskStatus(epicTask);
    }

    public ArrayList<PartEpicTask> getListOfAllPartEpicTaskExactEpic(EpicTask epicTask){
        ArrayList<PartEpicTask> listOfAllPartEpicTaskExactEpic = new ArrayList<>();

        ArrayList<Integer> listPartTaskId = epicTask.getListPartTaskId();
        for (Integer id : listPartTaskId) {
            PartEpicTask partEpicTaskExactEpic = this.getPartEpicTaskById(id);
            listOfAllPartEpicTaskExactEpic.add(partEpicTaskExactEpic);
        }

        return listOfAllPartEpicTaskExactEpic;
    }

    private void updateEpicTaskStatus(EpicTask epicTask){
        ArrayList<Integer> listPartTaskId = epicTask.getListPartTaskId();
        boolean isAllCurrentStatusOfPartEpicTaskNew = true;
        boolean isAllCurrentStatusOfPartEpicTaskDone = true;

        if (listPartTaskId.isEmpty()){
            epicTask.setStatus(TaskStatus.NEW);
            return;
        }

        for (Integer id : listPartTaskId) {
            PartEpicTask partEpicTask = partEpicTaskMap.get(id);
            TaskStatus currentStatusOfPartEpicTask = partEpicTask.getStatus();
            if (currentStatusOfPartEpicTask == TaskStatus.IN_PROGRESS){
                epicTask.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
            if (currentStatusOfPartEpicTask == TaskStatus.DONE){
                isAllCurrentStatusOfPartEpicTaskNew = false;
            }
            if (currentStatusOfPartEpicTask == TaskStatus.NEW){
                isAllCurrentStatusOfPartEpicTaskDone = false;
            }
            if (!isAllCurrentStatusOfPartEpicTaskDone && !isAllCurrentStatusOfPartEpicTaskNew){
                epicTask.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }

        if (isAllCurrentStatusOfPartEpicTaskNew){
            epicTask.setStatus(TaskStatus.NEW);
            return;
        }

        epicTask.setStatus(TaskStatus.DONE);
    }
}