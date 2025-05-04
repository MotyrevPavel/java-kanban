package tasks;

import java.util.Objects;

public class SimpleTask {
    private static int idGenerator = 0;
    int id;
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

    public SimpleTask(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        id = idGenerator;
        idGenerator++;
    }

    public SimpleTask(String name, String description, TaskStatus status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SimpleTask that = (SimpleTask) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
