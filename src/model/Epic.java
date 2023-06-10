package model;


import service.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subsId = new ArrayList<>();


     public Epic(String name, String description, int id) {
        super(name, description,id, TaskStatus.NEW, TaskType.EPIC);

    }

    public List<Integer> getSubsId() {
        return subsId;
    }

    public void setSubsId(ArrayList<Integer> subsId) {
         this.subsId = subsId;
    }
}
