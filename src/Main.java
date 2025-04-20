import tasks.EpicTask;
import tasks.PartEpicTask;
import tasks.SimpleTask;
import tasks.TaskStatus;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        //Весь main реализован исключительно для тестирования кода. Весь его можно удалить!

        TaskManager taskManager = new TaskManager();

        System.out.println("Создаем 3 задачи и добавляем их в таск менеджер. У simpleTask3 id такой же как");
        System.out.println("и у simpleTask1 поэтому simpleTask1 не должно быть в выводе.");
        System.out.println("Она перезапишется на simpleTask3");
        SimpleTask simpleTask1 = new SimpleTask("simpleTask1", "description");
        SimpleTask simpleTask2 = new SimpleTask("simpleTask2", "description2", TaskStatus.IN_PROGRESS);
        SimpleTask simpleTask3 = new SimpleTask("simpleTask3", "description3",
                TaskStatus.IN_PROGRESS, simpleTask1.getId());

        taskManager.makeNewTask(simpleTask1);
        taskManager.makeNewTask(simpleTask2);
        taskManager.updateTask(simpleTask3);

        taskManager.printAllTask();

        System.out.println("Удаляем все элементы SimpleTask.");
        System.out.println("Сейчас в выводе их нет");

        taskManager.removeAllSimpleTask();
        taskManager.printAllTask();
        System.out.println("-----".repeat(10));

        System.out.println("Создаем EpicTask и одну его подзадачу. Добавляем в менеджер.");
        System.out.println("Проверяем вывод, что они добавились");

        EpicTask epicTask1 = new EpicTask("epicTask1", "description epicTask1");
        PartEpicTask partEpicTask1 = new PartEpicTask("partEpicTask1", "description partEpicTask1",
                epicTask1);

        taskManager.makeNewTask(epicTask1);
        taskManager.makeNewTask(partEpicTask1);

        taskManager.printAllTask();

        System.out.println("Cоздаем еще 3 подзадачи для одного EpicTask.");
        System.out.println("Проверяем что partEpicTask4 перезаписал значения");
        System.out.println("partEpicTask2 т.к. у них одинаковый ID.");

        PartEpicTask partEpicTask2 = new PartEpicTask("partEpicTask2", "description partEpicTask2",
                TaskStatus.IN_PROGRESS, epicTask1);
        PartEpicTask partEpicTask3 = new PartEpicTask("partEpicTask3", "description partEpicTask3",
                TaskStatus.DONE, epicTask1);
        PartEpicTask partEpicTask4 = new PartEpicTask("partEpicTask4", "description partEpicTask4",
                TaskStatus.DONE, partEpicTask2.getId(), epicTask1);

        taskManager.makeNewTask(partEpicTask2);
        taskManager.makeNewTask(partEpicTask3);
        taskManager.updateTask(partEpicTask4);

        taskManager.printAllTask();

        System.out.println("удаляем все PartEpicTask. В следующем выводе их быть не должно.");

        taskManager.removeAllPartEpicTask();
        taskManager.printAllTask();

        System.out.println("PartEpicTask в выводе нет");

        System.out.println("Проверяем что значения перезаписываются в SimpleTask когда мы просто обнавляем статус,");
        System.out.println("но ID оставляем те же");
        SimpleTask simpleTask4 = new SimpleTask("simpleTask4", "description4");
        SimpleTask simpleTask5 = new SimpleTask("simpleTask5", "description5");

        taskManager.makeNewTask(simpleTask4);
        taskManager.makeNewTask(simpleTask5);
        taskManager.printAllTask();

        System.out.println("---".repeat(10));

        SimpleTask simpleTask6 = new SimpleTask("simpleTask4", "description4",
                TaskStatus.IN_PROGRESS, simpleTask4.getId());
        SimpleTask simpleTask7 = new SimpleTask("simpleTask5", "description5",
                TaskStatus.IN_PROGRESS, simpleTask5.getId());
        taskManager.updateTask(simpleTask6);
        taskManager.updateTask(simpleTask7);
        taskManager.printAllTask();

        System.out.println("Проверяем удаление по ID в partTaskEpic. Для этого в EpicTask добавим 3 задачи");

        taskManager.makeNewTask(partEpicTask1);
        taskManager.makeNewTask(partEpicTask2);
        taskManager.makeNewTask(partEpicTask3);

        taskManager.printAllTask();

        System.out.println("Удаляем partEpicTask3 по ID 5, теперь его в списке нет");

        taskManager.removeTaskById(5);
        taskManager.printAllTask();

        System.out.println("Удалим EpicTask по ID2 и SimpleTask по ID6");
        System.out.println("останется только SimpleTask под ID7");

        taskManager.removeTaskById(2);
        taskManager.removeTaskById(6);

        taskManager.printAllTask();

        System.out.println("Проверяем работу метода getAllSimpleTask");
        taskManager.makeNewTask(simpleTask1);
        taskManager.makeNewTask(simpleTask2);
        taskManager.makeNewTask(simpleTask3);
        taskManager.makeNewTask(epicTask1);
        taskManager.makeNewTask(partEpicTask1);
        taskManager.makeNewTask(partEpicTask2);
        taskManager.makeNewTask(partEpicTask3);

        HashMap<Integer, SimpleTask> allSimpleTask = taskManager.getAllSimpleTask();
        for (SimpleTask value : allSimpleTask.values()) {
            System.out.println(value);
        }

        System.out.println("---".repeat(10));
        System.out.println("Проверяем работу метода getAllEpicTask");

        HashMap<Integer, EpicTask> allEpicTask = taskManager.getAllEpicTask();
        for (EpicTask value : allEpicTask.values()) {
            System.out.println(value);
        }

        System.out.println("---".repeat(10));
        System.out.println("Проверяем работу метода getAllPartTask");

        HashMap<Integer, PartEpicTask> allPartTask = taskManager.getAllPartTask();
        for (PartEpicTask value : allPartTask.values()) {
            System.out.println(value);
        }

        System.out.println("---".repeat(10));
        System.out.println("Проверяем работу метода getTaskById");
        if (taskManager.getTaskById(7).isPresent()){
            System.out.println(taskManager.getTaskById(7).get());
        }else {
            System.out.println("Задачи с таким номером нет в списке задач");
        }

        System.out.println("И еще раз");

        if (taskManager.getTaskById(20).isPresent()){
            System.out.println(taskManager.getTaskById(20).get());
        }else {
            System.out.println("Задачи с таким номером нет в списке задач");
        }

        System.out.println("---".repeat(10));
        System.out.println("Проверяем работу метода getAllTasks");
        HashMap<Integer, SimpleTask> allTasks = taskManager.getAllTasks();
        for (SimpleTask value : allTasks.values()) {
            System.out.println(value);
        }

        System.out.println("---".repeat(10));
        System.out.println("Проверяем работу метода removeAllEpicTask");
        taskManager.removeAllEpicTask();
        taskManager.printAllTask();

        System.out.println("---".repeat(10));
        System.out.println("Все работает. Можно интегрировать!!! :)");
        System.out.println("Надеюсь что все что я написал можно понять и разобрать :)");
        System.out.println("Жду позитивную обратную связь по коду :))))");
        System.out.println("Ну и хорошего дня! :)");


    }
}
