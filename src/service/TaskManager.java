package service;


import model.Epic;
import model.SimpleTask;
import model.SubTask;

import java.util.ArrayList;

public interface TaskManager {
    public ArrayList<SimpleTask> getListOfSimpleTasks();


     ArrayList<Epic> getListOfEpics();

     ArrayList<SubTask> getListOfsubTasks();

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

     ArrayList<SubTask> getListOfSubsOfEpic(Epic epic);
}