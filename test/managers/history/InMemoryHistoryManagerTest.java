package managers.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.SimpleTask;

import java.lang.reflect.Field;
import java.util.List;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    List<SimpleTask> viewedTasksHistory;

    @BeforeEach
    public void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        try {
            Field fieldViewedTasksHistory = inMemoryHistoryManager.getClass()
                    .getDeclaredField("viewedTasksHistory");
            fieldViewedTasksHistory.setAccessible(true);
            viewedTasksHistory = (List<SimpleTask>) fieldViewedTasksHistory.get(inMemoryHistoryManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldReturnTrueWhenTasksHistoryEqualsTaskHistoryInMemoryHistoryManager() {
        //Given
        viewedTasksHistory.add(new SimpleTask("test0", "test0"));
        viewedTasksHistory.add(new SimpleTask("test1", "test1"));
        viewedTasksHistory.add(new SimpleTask("test2", "test2"));
        viewedTasksHistory.add(new SimpleTask("test3", "test3"));
        //When
        List<SimpleTask> taskHistoryInMemoryHistoryManager = inMemoryHistoryManager.getHistory();
        //Then
        Assertions.assertArrayEquals(viewedTasksHistory.toArray(), taskHistoryInMemoryHistoryManager.toArray());
    }

    @Test
    public void shouldReturnTrueWhenAddedTwoTaskAndEqualsWithEightPositionInViewedTasksHistory() {
        //Given
        SimpleTask simpleTask = new SimpleTask("test0", "test0");
        SimpleTask simpleTask2 = new SimpleTask("test1", "test1");
        //When
        inMemoryHistoryManager.add(simpleTask);
        inMemoryHistoryManager.add(simpleTask2);
        //Then
        Assertions.assertEquals(viewedTasksHistory.get(1), simpleTask2);
    }
}