package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subsId = new ArrayList<>();

     public Epic(String name, String description, int id, String status) {
        super(name, description, id, status);
    }

    public ArrayList<Integer> getSubsId() {
        return subsId;
    }

    public void setSubsId(ArrayList<Integer> subsId) {
         this.subsId = subsId;
    }
}
