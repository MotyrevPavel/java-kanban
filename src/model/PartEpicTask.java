package model;

import util.TaskStatus;
import util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class PartEpicTask extends SimpleTask {
    private final Integer idConnectEpicTask;

    public PartEpicTask(Integer id, String name, String description, TaskStatus status,
                        LocalDateTime startTime, Duration durationInMinutes, Integer idConnectEpicTask) {
        super(id, name, description, status, startTime, durationInMinutes);
        this.idConnectEpicTask = idConnectEpicTask;
    }

    public PartEpicTask(String name, String description, LocalDateTime startTime, Duration duration,
                        Integer idConnectEpicTask) {
        super(name, description, startTime, duration);
        this.idConnectEpicTask = idConnectEpicTask;
    }

    public PartEpicTask(String name, String description, Integer idConnectEpicTask) {
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
        Long stringDuration = super.getDuration().isPresent() ? super.getDuration().get().toMinutes() : null;
        LocalDateTime stringStartTime = super.getStartTime().orElse(null);

        return super.getId() +
                "," + TaskType.PARTEPIC +
                "," + super.getName() +
                "," + super.getStatus() +
                "," + super.getDescription() +
                ',' + idConnectEpicTask +
                ',' + stringStartTime +
                ',' + stringDuration;
    }
}