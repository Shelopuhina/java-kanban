package model;

import service.EpicStatus;

public class Task {
    private String name;
    private String description;
    private int id;
    private EpicStatus status;

    public Task(String name, String description, int id, EpicStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public EpicStatus getStatus() {
        return status;
    }

    public void setStatus(EpicStatus status) {
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


}
