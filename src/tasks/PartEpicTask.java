package tasks;

public class PartEpicTask extends SimpleTask{
    private final int idConnectEpicTask;

    public PartEpicTask(String name, String description, int idConnectEpicTask) {
        super(name, description);
        this.idConnectEpicTask = idConnectEpicTask;
    }

    public PartEpicTask(String name, String description, TaskStatus status, int idConnectEpicTask) {
        super(name, description, status);
        this.idConnectEpicTask = idConnectEpicTask;
    }

    public PartEpicTask(String name, String description, TaskStatus status, int id, int idConnectEpicTask) {
        super(name, description, status, id);
        this.idConnectEpicTask = idConnectEpicTask;
    }

    public int getIdConnectEpicTask() {
        return idConnectEpicTask;
    }

    @Override
    public String toString() {
        return "PartEpicTask{" +
                "id=" + id +
                ", idConnectEpicTask=" + idConnectEpicTask +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
