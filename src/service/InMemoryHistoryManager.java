package service;

import model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> history = new LinkedList<>();//узнала, что у List 4 реализации, 2 устаревшие, Array и Linked.
                                                    //LinkedList считаетсся более быстрой реализацией из-за ссылок?
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
