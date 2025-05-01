package managers;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;

public class Managers {
    public static TaskManager getTaskManager(Class type){
        if(type == InMemoryTaskManager.class){
            return new InMemoryTaskManager();
        }
        return getDefault();
    }

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
