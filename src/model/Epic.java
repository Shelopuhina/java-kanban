package model;


import model.enums.TaskStatus;
import model.enums.TaskType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subsId = new ArrayList<>();
    private Instant endTime;


     public Epic(String name, String description, int id) {
        super(name, description,id, TaskStatus.NEW, TaskType.EPIC,0, Instant.ofEpochSecond(0));


    }

    public List<Integer> getSubsId() {
        return subsId;
    }

    public void setSubsId(ArrayList<Integer> subsId) {
         this.subsId = subsId;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
}
