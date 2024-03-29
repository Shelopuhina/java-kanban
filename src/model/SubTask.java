package model;


import model.enums.TaskStatus;
import model.enums.TaskType;

import java.time.Instant;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int id, TaskStatus status, long duration, Instant startTime, int epicId) {
        super(name, description, id, status, TaskType.SUBTASK, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}
