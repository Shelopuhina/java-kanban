package model;

import service.EpicStatus;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int id, EpicStatus status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}
