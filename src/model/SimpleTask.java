package model;

import util.TaskStatus;
import util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class SimpleTask implements Cloneable {
    private static int idGenerator = 0;
    private final Integer id;
    private final String name;
    private final String description;
    private final TaskStatus status;
    private final LocalDateTime startTime;
    private final Duration duration;

    public SimpleTask(Integer id, String name, String description, TaskStatus status,
                      LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public SimpleTask(String name, String description, LocalDateTime startTime, Duration duration) {
        this(idGenerator++, name, description, TaskStatus.NEW, startTime, duration);
    }

    public SimpleTask(String name, String description) {
        this(idGenerator++, name, description, TaskStatus.NEW, null, null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }


    public Optional<LocalDateTime> getEndTime() {
        try {
            return Optional.of(startTime.plusMinutes(duration.toMinutes()));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public SimpleTask clone() throws CloneNotSupportedException {
        return (SimpleTask) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SimpleTask that = (SimpleTask) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        Long stringDuration = getDuration().isPresent() ? duration.toMinutes() : null;

        return id +
                "," + TaskType.SIMPLE +
                "," + name +
                "," + status +
                "," + description +
                ',' + name +
                ',' + startTime +
                ',' + stringDuration;
    }
}