package service;

import model.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> history = new LinkedList<>();//поняла,спасибо!<3

    @Override
    public void addTask(Task task) {
        if(history.size()<limit) {
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
