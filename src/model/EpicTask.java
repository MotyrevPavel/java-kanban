package model;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends SimpleTask {
    private final List<Integer> listPartTaskId;

    public EpicTask(String name, String description) {
        super(name, description);
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
        return "EpicTask{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                '}' + System.lineSeparator();
    }
}
