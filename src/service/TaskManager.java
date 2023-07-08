package service;


import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TaskManager {

  Map<Integer, SimpleTask> getSimpleTask() ;

  Map<Integer, Epic> getEpics() ;

  Map<Integer, SubTask> getSubTasks();
    List<SimpleTask> getListOfSimpleTasks() throws IOException;


     List<Epic> getListOfEpics();

     List<SubTask> getListOfsubTasks();

     void deleteAllSimpleTasks() throws IOException;

     void deleteAllEpics() throws IOException;

     void deleteAllSubTasks() throws IOException;

     SimpleTask getTaskById(int id) throws IOException;

     Epic getEpicById(int id) throws IOException;

     SubTask getSubTaskById(int id) throws IOException;

     int addSimpleTask(SimpleTask task) throws IOException;

     int addEpic(Epic epic) throws IOException;

     int addSubTask(SubTask subTask) throws IOException;

     void updateSimpleTask(SimpleTask task) throws IOException;

     void updateEpic(Epic epic) throws IOException;

     void updateSubTask(SubTask subTask) throws IOException;

     void deleteSimpleTaskById(int id) throws IOException;

     void deleteEpicById(int id) throws IOException;

     void deleteSubTaskById(int id) throws IOException;

     List<SubTask> getListOfSubsOfEpic(Epic epic);
     List<Task> getHistory() throws IOException;
     List<Task> getPrioritizedTasks() throws IOException;
}