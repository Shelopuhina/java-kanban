package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> history = new ArrayList<Task>();

    @Override
    public void addTask(Task task) {
        if(history.size()<10) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }


}
