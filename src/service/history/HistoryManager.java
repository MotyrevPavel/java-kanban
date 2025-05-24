package service.history;

import model.SimpleTask;

import java.util.List;

public interface HistoryManager {
    void add(SimpleTask task) throws CloneNotSupportedException;
    List<SimpleTask> getHistory();
    void remove(int id);
}
