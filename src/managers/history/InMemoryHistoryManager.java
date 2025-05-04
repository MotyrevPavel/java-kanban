package managers.history;

import tasks.SimpleTask;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<SimpleTask> viewedTasksHistory;

    public InMemoryHistoryManager(){
        this.viewedTasksHistory = new ArrayList<>();
    }

    public List<SimpleTask> getHistory(){
        return new ArrayList<>(viewedTasksHistory);
    }

    public void add(SimpleTask task){
        if (viewedTasksHistory.size() == 10){
            viewedTasksHistory.removeFirst();
        }
        viewedTasksHistory.add(task);
    }
}
