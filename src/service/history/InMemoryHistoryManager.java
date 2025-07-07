package service.history;

import service.LinkedMap;
import model.SimpleTask;

import java.util.List;
import java.util.Set;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedMap<SimpleTask> viewedTasksHistory;

    public InMemoryHistoryManager() {
        this.viewedTasksHistory = new LinkedMap<>();
    }

    public List<SimpleTask> getHistory() {
        return viewedTasksHistory.getAllValues();
    }

    public void add(SimpleTask task) {
        viewedTasksHistory.add(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        viewedTasksHistory.removeItemById(id);
    }

    @Override
    public void remove(Set<Integer> removedTasksId) {
        viewedTasksHistory.getAllValues()
                .stream()
                .map(SimpleTask::getId)
                .forEach(id -> {
                    if (removedTasksId.contains(id)) {
                        viewedTasksHistory.removeItemById(id);
                    }
                });
    }
}
