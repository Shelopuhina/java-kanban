package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    int limit = 10;

    void  addTask(Task task);
    List<Task> getHistory();
}
