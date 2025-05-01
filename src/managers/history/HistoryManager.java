package managers.history;

import tasks.SimpleTask;

public interface HistoryManager {
    void add(SimpleTask task);
    SimpleTask[] getHistory();
}
