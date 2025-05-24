package service.task;

import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;

import java.util.List;

public interface TaskManager {

    List<SimpleTask> getAllSimpleTask();

    List<EpicTask> getAllEpicTask();

    List<PartEpicTask> getAllPartEpicTask();

    void removeAllSimpleTask();

    void removeAllEpicTask();

    void removeAllPartEpicTask();

    SimpleTask getSimpleTaskById(int id);

    EpicTask getEpicTaskById(int id);

    PartEpicTask getPartEpicTaskById(int id);

    void makeNewSimpleTask(SimpleTask simpleTask);

    void makeNewEpicTask(EpicTask epicTask);

    void makeNewPartEpicTask(PartEpicTask partEpicTask);

    void updateSimpleTask(SimpleTask simpleTask);

    void updateEpicTask(EpicTask epicTask);

    void updatePartEpicTask(PartEpicTask partEpicTask);

    void removeSimpleTaskById(int id);

    void removeEpicTaskById(int id);

    void removePartEpicTaskById(int id);

    List<SimpleTask> getHistory();

    List<PartEpicTask> getListOfAllPartEpicTaskExactEpic(EpicTask epicTask);
}
