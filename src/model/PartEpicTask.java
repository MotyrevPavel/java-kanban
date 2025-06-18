package model;

public class PartEpicTask extends SimpleTask {
    private final int idConnectEpicTask;

    public PartEpicTask(String name, String description, int idConnectEpicTask) {
        super(name, description);
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
        return "PartEpicTask{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                '}' + System.lineSeparator();
    }
}
