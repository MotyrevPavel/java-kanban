package managers.task;

import tasks.EpicTask;
import tasks.PartEpicTask;
import tasks.SimpleTask;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<SimpleTask> getAllSimpleTask();

    ArrayList<EpicTask> getAllEpicTask();

    ArrayList<PartEpicTask> getAllPartEpicTask();

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

    SimpleTask[] getHistory();

    ArrayList<PartEpicTask> getListOfAllPartEpicTaskExactEpic(EpicTask epicTask);
}
