package model;

import util.TaskStatus;
import util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EpicTask extends SimpleTask {
    private final LocalDateTime endTime;
    private final List<Integer> listPartTaskId;

    public EpicTask(Integer id, String name, String description, TaskStatus status,
                    LocalDateTime startTime, Duration duration, LocalDateTime endTime, List<Integer> listPartTaskId) {
        super(id, name, description, status, startTime, duration);
        this.endTime = endTime;
        this.listPartTaskId = List.copyOf(listPartTaskId);
    }

    public EpicTask(Integer id, String name, String description, TaskStatus status,
                    LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.endTime = null;
        this.listPartTaskId = new ArrayList<>();
    }

    public EpicTask(String name, String description) {
        super(name, description);
        this.endTime = null;
        this.listPartTaskId = new ArrayList<>();
    }

    public List<Integer> getListPartTaskId() {
        return new ArrayList<>(listPartTaskId);
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    @Override
    public EpicTask clone() throws CloneNotSupportedException {
        return (EpicTask) super.clone();
    }

    @Override
    public String toString() {
        Long stringDuration = getDuration().isPresent() ? getDuration().get().toMinutes() : null;
        LocalDateTime stringStartTime = super.getStartTime().orElse(null);

        return super.getId() +
                "," + TaskType.EPIC +
                "," + super.getName() +
                "," + super.getStatus() +
                "," + super.getDescription() +
                ',' + super.getName() +
                ',' + stringStartTime +
                ',' + stringDuration;
    }
}