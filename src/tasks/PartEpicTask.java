package tasks;

public class PartEpicTask extends SimpleTask{
    private final EpicTask linkToConnectEpicTask;

    public PartEpicTask(String name, String description, EpicTask linkToConnectEpicTask) {
        super(name, description);
        this.linkToConnectEpicTask = linkToConnectEpicTask;
    }

    public PartEpicTask(String name, String description, TaskStatus status, EpicTask linkToConnectEpicTask) {
        super(name, description, status);
        this.linkToConnectEpicTask = linkToConnectEpicTask;
    }

    public PartEpicTask(String name, String description, TaskStatus status, int id, EpicTask linkToConnectEpicTask) {
        super(name, description, status, id);
        this.linkToConnectEpicTask = linkToConnectEpicTask;
    }

    public EpicTask getLinkToConnectEpicTask() {
        return linkToConnectEpicTask;
    }

    @Override
    public String toString() {
        return "PartEpicTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
