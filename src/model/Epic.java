package model;


import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subsId = new ArrayList<>();


     public Epic(String name, String description, int id) {
        super(name, description,id, TaskStatus.NEW);

    }

    public ArrayList<Integer> getSubsId() {
        return subsId;
    }

    public void setSubsId(ArrayList<Integer> subsId) {
         this.subsId = subsId;
    }
}
