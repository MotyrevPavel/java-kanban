import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;
import service.task.TaskManager;
import util.Managers;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getTaskManager();

        System.out.println();
        System.out.println("-----".repeat(10));
        System.out.println();

        SimpleTask simpleTask1 = new SimpleTask("SimpleTask1", "Description SimpleTask1");
        SimpleTask simpleTask2 = new SimpleTask("SimpleTask2", "Description SimpleTask2");
        EpicTask epicTaskWithoutSubTask = new EpicTask("Epic1", "epicTaskWithoutSubTask");
        EpicTask epicTask = new EpicTask("Epic2", "Epic with 3 subtask");
        PartEpicTask partEpicTask1 = new PartEpicTask("partEpicTask1", "Description partEpicTask1",
                epicTask.getId());
        PartEpicTask partEpicTask2 = new PartEpicTask("partEpicTask2", "Description partEpicTask2",
                epicTask.getId());
        PartEpicTask partEpicTask3 = new PartEpicTask("partEpicTask3", "Description partEpicTask3",
                epicTask.getId());


        taskManager.makeNewSimpleTask(simpleTask1);
        taskManager.makeNewSimpleTask(simpleTask2);

        taskManager.makeNewEpicTask(epicTaskWithoutSubTask);
        taskManager.makeNewEpicTask(epicTask);

        taskManager.makeNewPartEpicTask(partEpicTask1);
        taskManager.makeNewPartEpicTask(partEpicTask2);
        taskManager.makeNewPartEpicTask(partEpicTask3);

        System.out.println(taskManager.getHistory());

        System.out.println();
        System.out.println("-----".repeat(10));
        System.out.println();

        taskManager.getSimpleTaskById(simpleTask2.getId());
        taskManager.getPartEpicTaskById(partEpicTask2.getId());

        System.out.println(taskManager.getHistory());

        System.out.println();
        System.out.println("-----".repeat(10));
        System.out.println();

        taskManager.getEpicTaskById(epicTask.getId());
        taskManager.getEpicTaskById(epicTask.getId());
        taskManager.getEpicTaskById(epicTask.getId());

        System.out.println(taskManager.getHistory());

        System.out.println();
        System.out.println("-----".repeat(10));
        System.out.println();

        taskManager.removeEpicTaskById(epicTask.getId());
        System.out.println(taskManager.getHistory());
    }
}
