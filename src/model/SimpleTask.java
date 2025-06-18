package model;

import util.TaskStatus;
import util.TaskType;

public class SimpleTask implements Cloneable {
    private static int idGenerator = 0;
    private final int id;
    private final String name;
    private final String description;
    private TaskStatus status;

    public SimpleTask(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        id = idGenerator;
        idGenerator++;
    }

    public SimpleTask(int id, String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        this.id = id;
    }

    public SimpleTask(int id, String name, TaskStatus status, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public SimpleTask clone() throws CloneNotSupportedException {
        return (SimpleTask) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SimpleTask that = (SimpleTask) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return id +
                "," + TaskType.SIMPLE +
                "," + name +
                "," + status +
                "," + description +
                ',' + name;
    }
}