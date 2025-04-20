package tasks;

import java.util.HashMap;
import java.util.Optional;

public class EpicTask extends SimpleTask{
    private final HashMap<Integer, PartEpicTask> partTaskMap;

    public EpicTask(String name, String description) {
        super(name, description);
        this.partTaskMap = new HashMap<>();
    }

    public HashMap<Integer, PartEpicTask> getPartTaskMap(){
        return new HashMap<>(partTaskMap);
    }

    public Optional<PartEpicTask> getPartEpicTaskById(int idPartEpicTask){
        if (partTaskMap.isEmpty()) {
            return Optional.empty();
        }
        if (partTaskMap.containsKey(idPartEpicTask)) {
            return Optional.of(partTaskMap.get(idPartEpicTask));
        }
        return Optional.empty();
    }

    public boolean containsPartEpicTaskKey(int id){
        return partTaskMap.containsKey(id);
    }

    public void printAll(){
        for (PartEpicTask partEpicTask : partTaskMap.values()) {
            System.out.println(partEpicTask);
        }
    }

    public void removePartEpicTask(int id){
        partTaskMap.remove(id);
        updateEpicTaskStatus();
    }

    public void putPartEpicTask(PartEpicTask task){
        partTaskMap.put(task.id, task);
        updateEpicTaskStatus();
    }

    public void removeAllPartTaskMap(){
        partTaskMap.clear();
        updateEpicTaskStatus();
    }

    private void updateEpicTaskStatus(){
        if (partTaskMap.isEmpty()){
            this.status = TaskStatus.NEW;
            return;
        }
        for (PartEpicTask partTask : partTaskMap.values()) {
            if (partTask.status != TaskStatus.DONE){
                boolean isAllPartEpicTaskNew = isAllPartEpicTaskNew();
                if (!isAllPartEpicTaskNew){
                    this.status = TaskStatus.IN_PROGRESS;
                }else {
                    this.status = TaskStatus.NEW;
                }
                return;
            }
        }
        this.status = TaskStatus.DONE;
    }

    private boolean isAllPartEpicTaskNew(){
        for (PartEpicTask partEpicTask : partTaskMap.values()) {
            if (partEpicTask.status != TaskStatus.NEW){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
