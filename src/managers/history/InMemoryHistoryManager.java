package managers.history;

import tasks.SimpleTask;

public class InMemoryHistoryManager implements HistoryManager{
    private final SimpleTask[] viewedTasksHistory;

    public InMemoryHistoryManager(){
        this.viewedTasksHistory = new SimpleTask[10];
    }

    public SimpleTask[] getHistory(){
        SimpleTask[] newViewedTasksHistory = new SimpleTask[10];
        for (int i = 0; i < newViewedTasksHistory.length; i++) {
            newViewedTasksHistory[i] = viewedTasksHistory[i];
        }
        return newViewedTasksHistory;
    }

    public void add(SimpleTask task){
        for (int i = 0; i < viewedTasksHistory.length; i++) {
            if (i+1 < viewedTasksHistory.length){
                viewedTasksHistory[i] = viewedTasksHistory[i+1];
            }else {
                viewedTasksHistory[i] = task;
            }
        }
    }
}
