package managers.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.SimpleTask;

import java.lang.reflect.Field;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    SimpleTask[] viewedTasksHistory;

    @BeforeEach
    public void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        try {
            Field fieldViewedTasksHistory = inMemoryHistoryManager.getClass()
                    .getDeclaredField("viewedTasksHistory");
            fieldViewedTasksHistory.setAccessible(true);
            viewedTasksHistory = (SimpleTask[]) fieldViewedTasksHistory.get(inMemoryHistoryManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldReturnTrueWhenTasksHistoryEqualsTaskHistoryInMemoryHistoryManager() {
        //Given
        viewedTasksHistory[0] = new SimpleTask("test0", "test0");
        viewedTasksHistory[1] = new SimpleTask("test1", "test1");
        viewedTasksHistory[2] = new SimpleTask("test2", "test2");
        viewedTasksHistory[3] = new SimpleTask("test3", "test3");
        //When
        SimpleTask[] taskHistoryInMemoryHistoryManager = inMemoryHistoryManager.getHistory();
        //Then
        Assertions.assertArrayEquals(viewedTasksHistory, taskHistoryInMemoryHistoryManager);
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
        Assertions.assertEquals(viewedTasksHistory[8], simpleTask);
    }
}