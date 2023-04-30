package service;


import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<SimpleTask> getListOfSimpleTasks();


     List<Epic> getListOfEpics();

     List<SubTask> getListOfsubTasks();

     void deleteAllSimpleTasks();

     void deleteAllEpics();

     void deleteAllSubTasks();

     SimpleTask getTaskById(int id);

     Epic getEpicById(int id);

     SubTask getSubTaskById(int id);

     int addSimpleTask(SimpleTask task);

     int addEpic(Epic epic);

     int addSubTask(SubTask subTask);

     void updateSimpleTask(SimpleTask task);

     void updateEpic(Epic epic);

     void updateSubTask(SubTask subTask);

     void deleteSimpleTaskById(int id);

     void deleteEpicById(int id) ;

     void deleteSubTaskById(int id);

     List<SubTask> getListOfSubsOfEpic(Epic epic);
     List<Task> getHistory();
}