package util;

import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;
import service.task.InMemoryTaskManager;
import service.task.TaskManager;

public class Managers {
    public static TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
