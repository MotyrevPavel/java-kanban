package service.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.SimpleTask;
import service.LinkedMap;

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
        viewedTasksHistory.add(0, new SimpleTask("test0", "test0", 0));
        viewedTasksHistory.add(1, new SimpleTask("test1", "test1", 1));
        //When
        inMemoryHistoryManager.remove(0);
        inMemoryHistoryManager.remove(1);
        //Then
        Assertions.assertTrue(viewedTasksHistory.getAllValues().isEmpty());
    }

    @Test
    public void shouldReturnTrueWhenReturnCorrectHistory() {
        viewedTasksHistory.add(0, new SimpleTask("test0", "test0", 0));
        viewedTasksHistory.add(1, new SimpleTask("test1", "test1", 1));
        //When
        List<SimpleTask> listSimpleTask = inMemoryHistoryManager.getHistory();
        //Then
        Assertions.assertArrayEquals(viewedTasksHistory.getAllValues().toArray(), listSimpleTask.toArray());
    }
}