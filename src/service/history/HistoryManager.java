package service.history;

import model.SimpleTask;

import java.util.List;
import java.util.Set;

public interface HistoryManager {
    void add(SimpleTask task) throws CloneNotSupportedException;

    List<SimpleTask> getHistory();

    void remove(int id);

    void remove(Set<Integer> removedTasksId);
}
