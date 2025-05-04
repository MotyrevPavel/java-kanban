package managers.history;

import tasks.SimpleTask;

import java.util.List;

public interface HistoryManager {
    void add(SimpleTask task);
    List<SimpleTask> getHistory();
}
