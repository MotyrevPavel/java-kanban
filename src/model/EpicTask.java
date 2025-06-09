package model;

import util.TaskStatus;
import util.TaskType;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends SimpleTask {
    private final List<Integer> listPartTaskId;

    public EpicTask(String name, String description) {
        super(name, description);
        this.listPartTaskId = new ArrayList<>();
    }

    public EpicTask(int id, String name, TaskStatus status, String description) {
        super(id, name, status, description);
        this.listPartTaskId = new ArrayList<>();
    }

    public void addPartTaskId(int id) {
        listPartTaskId.add(id);
    }

    public void removePartTaskById(int id) {
        listPartTaskId.remove(Integer.valueOf(id));
    }

    public void removeAllPartTask() {
        listPartTaskId.clear();
    }

    public List<Integer> getListPartTaskId() {
        return new ArrayList<>(listPartTaskId);
    }

    @Override
    public EpicTask clone() throws CloneNotSupportedException {
        return (EpicTask) super.clone();
    }

    @Override
    public String toString() {
        return super.getId() +
                "," + TaskType.EPIC +
                "," + super.getName() +
                "," + super.getStatus() +
                "," + super.getDescription() +
                ',' + super.getName();
    }
}