package service.task;

import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    List<SimpleTask> getAllSimpleTask();

    List<EpicTask> getAllEpicTask();

    List<PartEpicTask> getAllPartEpicTask();

    void removeAllSimpleTask();

    void removeAllEpicTask();

    void removeAllPartEpicTask();

    SimpleTask getSimpleTaskById(Integer id);

    EpicTask getEpicTaskById(Integer id);

    PartEpicTask getPartEpicTaskById(Integer id);

    void makeNewSimpleTask(SimpleTask simpleTask);

    void makeNewEpicTask(EpicTask epicTask);

    void makeNewPartEpicTask(PartEpicTask partEpicTask);

    void updateSimpleTask(SimpleTask simpleTask);

    void updateEpicTask(EpicTask epicTask);

    void updatePartEpicTask(PartEpicTask partEpicTask);

    void removeSimpleTaskById(Integer id);

    void removeEpicTaskById(Integer id);

    void removePartEpicTaskById(Integer id);

    List<SimpleTask> getHistory();

    List<PartEpicTask> getEpicSubtasks(EpicTask epicTask);

    Set<SimpleTask> getPrioritizedTasks();

    boolean validateScheduleConflict(SimpleTask firstTask, SimpleTask secondTask);
}
