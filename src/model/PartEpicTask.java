package model;

import util.TaskStatus;
import util.TaskType;

public class PartEpicTask extends SimpleTask {
    private final int idConnectEpicTask;

    public PartEpicTask(String name, String description, int idConnectEpicTask) {
        super(name, description);
        this.idConnectEpicTask = idConnectEpicTask;
    }

    public PartEpicTask(int id, String name, TaskStatus status, String description, int idConnectEpicTask) {
        super(id, name, status, description);
        this.idConnectEpicTask = idConnectEpicTask;
    }

    public int getIdConnectEpicTask() {
        return idConnectEpicTask;
    }

    @Override
    public PartEpicTask clone() throws CloneNotSupportedException {
        return (PartEpicTask) super.clone();
    }

    @Override
    public String toString() {
        return super.getId() +
                "," + TaskType.PARTEPIC +
                "," + super.getName() +
                "," + super.getStatus() +
                "," + super.getDescription() +
                ',' + idConnectEpicTask;
    }
}