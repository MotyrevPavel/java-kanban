package util;

import service.history.HistoryManager;
import service.task.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void shouldReturnTrueWhenGetTaskManagerReturnInMemoryTaskManager() {
        //When
        TaskManager inMemoryTaskManager = Managers.getTaskManager();
        //Then
        Assertions.assertNotNull(inMemoryTaskManager);
    }

    @Test
    void shouldReturnTrueWhenGetDefaultHistoryReturnInMemoryHistoryManager() {
        //When
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        //Then
        Assertions.assertNotNull(inMemoryHistoryManager);
    }
}