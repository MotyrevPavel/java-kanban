package service.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.SimpleTask;
import service.LinkedMap;
import util.TaskStatus;

import java.lang.reflect.Field;
import java.util.List;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    LinkedMap<SimpleTask> viewedTasksHistory;

    @BeforeEach
    public void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        try {
            Field fieldViewedTasksHistory = inMemoryHistoryManager.getClass()
                    .getDeclaredField("viewedTasksHistory");
            fieldViewedTasksHistory.setAccessible(true);
            viewedTasksHistory = (LinkedMap<SimpleTask>) fieldViewedTasksHistory.get(inMemoryHistoryManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void shouldReturnTrueWhenAddCorrectValueInMemoryHistoryManager() {
        //Given
        SimpleTask[] simpleTasks = {new SimpleTask("test0", "test0"),
                new SimpleTask("test0", "test0")};
        //When
        inMemoryHistoryManager.add(simpleTasks[0]);
        inMemoryHistoryManager.add(simpleTasks[1]);
        //Then
        Assertions.assertArrayEquals(viewedTasksHistory.getAllValues().toArray(), simpleTasks);
    }

    @Test
    public void shouldReturnTrueWhenTaskRemovedFromViewedTasksHistory() {
        viewedTasksHistory.add(0, new SimpleTask(0, "test0", "test0",
                TaskStatus.NEW, null, null));
        viewedTasksHistory.add(1, new SimpleTask(1, "test1", "test1",
                TaskStatus.NEW, null, null));
        //When
        inMemoryHistoryManager.remove(0);
        inMemoryHistoryManager.remove(1);
        //Then
        Assertions.assertTrue(viewedTasksHistory.getAllValues().isEmpty());
    }

    @Test
    public void shouldReturnTrueWhenReturnCorrectHistory() {
        viewedTasksHistory.add(0, new SimpleTask(0, "test0", "test0",
                TaskStatus.NEW, null, null));
        viewedTasksHistory.add(1, new SimpleTask(1, "test1", "test1",
                TaskStatus.NEW, null, null));
        //When
        List<SimpleTask> listSimpleTask = inMemoryHistoryManager.getHistory();
        //Then
        Assertions.assertArrayEquals(viewedTasksHistory.getAllValues().toArray(), listSimpleTask.toArray());
    }

    @Test
    public void shouldReturnTrueWhenHistoryIsEmpty() {
        //When
        List<SimpleTask> listSimpleTask = inMemoryHistoryManager.getHistory();
        //Then
        Assertions.assertTrue(listSimpleTask.isEmpty());
    }

    @Test
    public void shouldReturnTrueWhenHistoryNotHasDuplicateTask() {
        //Given
        SimpleTask task1 = new SimpleTask("task1", "task1");
        SimpleTask task2 = new SimpleTask("task2", "task2");
        SimpleTask task3 = new SimpleTask(task1.getId(), task1.getName(), task1.getDescription(), task1.getStatus(),
                task1.getStartTime().orElse(null), task1.getDuration().orElse(null));
        LinkedMap<SimpleTask> listSimpleTask = new LinkedMap<>();
        listSimpleTask.add(task1.getId(), task1);
        listSimpleTask.add(task2.getId(), task2);
        listSimpleTask.add(task3.getId(), task3);
        listSimpleTask.add(task2.getId(), task2);
        //When
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task2);
        //Then
        Assertions.assertEquals(listSimpleTask.getAllValues(), inMemoryHistoryManager.getHistory());
        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals(task2, inMemoryHistoryManager.getHistory().get(1));
    }

    @Test
    public void shouldReturnTrueWhenCorrectDeleteStartElementOfHistory() {
        //Given
        SimpleTask task1 = new SimpleTask("task1", "task1");
        SimpleTask task2 = new SimpleTask("task2", "task2");
        SimpleTask task3 = new SimpleTask("task3", "task3");
        SimpleTask task4 = new SimpleTask("task4", "task4");
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        //When
        inMemoryHistoryManager.remove(task1.getId());

        //Then
        Assertions.assertEquals(task2, inMemoryHistoryManager.getHistory().get(0));
        Assertions.assertEquals(task4, inMemoryHistoryManager.getHistory().get(2));
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void shouldReturnTrueWhenCorrectDeleteMiddleElementOfHistory() {
        //Given
        SimpleTask task1 = new SimpleTask("task1", "task1");
        SimpleTask task2 = new SimpleTask("task2", "task2");
        SimpleTask task3 = new SimpleTask("task3", "task3");
        SimpleTask task4 = new SimpleTask("task4", "task4");
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        //When
        inMemoryHistoryManager.remove(task3.getId());

        //Then
        Assertions.assertEquals(task1, inMemoryHistoryManager.getHistory().get(0));
        Assertions.assertEquals(task4, inMemoryHistoryManager.getHistory().get(2));
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void shouldReturnTrueWhenCorrectDeleteEndElementOfHistory() {
        //Given
        SimpleTask task1 = new SimpleTask("task1", "task1");
        SimpleTask task2 = new SimpleTask("task2", "task2");
        SimpleTask task3 = new SimpleTask("task3", "task3");
        SimpleTask task4 = new SimpleTask("task4", "task4");
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        //When
        inMemoryHistoryManager.remove(task4.getId());

        //Then
        Assertions.assertEquals(task1, inMemoryHistoryManager.getHistory().get(0));
        Assertions.assertEquals(task3, inMemoryHistoryManager.getHistory().get(2));
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }
}