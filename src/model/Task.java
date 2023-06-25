package model;


import service.TaskType;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;
    private TaskType type;
    private long duration;
    private Instant startTime;

    public Task(String name, String description, int id, TaskStatus status, TaskType type, long duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type;
        this.duration = duration;
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
    public Instant getEndTime() {
    return startTime.plusSeconds(duration*60);
}

}
