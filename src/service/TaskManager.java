package service;

import model.Epic;
import model.SimpleTask;
import model.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, SimpleTask> simpleTask = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int nextId = 1;
    private int nextIdEpic = 1;
    private int nextIdSub = 1;


    public ArrayList<SimpleTask> getListOfSimpleTasks() {
        return new ArrayList<>(simpleTask.values());

    }

    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getListOfsubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllSimpleTasks() {
        simpleTask.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasks() {

        for(Epic epic : epics.values()) {
            epic.getSubsId().clear();
            updateEpicStatus(epic);
        }
        subTasks.clear();
    }

    public SimpleTask getTaskById(int id) {
        return simpleTask.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public int addSimpleTask(SimpleTask task) {
        task.setId(nextId);
        nextId++;
        simpleTask.put(task.getId(), task);
        return task.getId();
    }

    public int addEpic(Epic epic) {
        epic.setId(nextIdEpic);
        nextIdEpic++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public int addSubTask(SubTask subTask) {
        subTask.setId(nextIdSub);
        nextIdSub++;
        subTasks.put(subTask.getId(), subTask);
        int epId = subTask.getEpicId();
        epics.get(epId).getSubsId().add(subTask.getId());
        updateEpicStatus(epics.get(subTask.getEpicId()));
        return subTask.getId();
    }

    private String updateEpicStatus(Epic epic) {
        String epicStatus = null;
        int countNew = 0;
        int countDone = 0;
        for (Integer integer : epic.getSubsId()) {
            if (epic.getSubsId().isEmpty()) {
                epicStatus = "NEW";
            } else {
                boolean isStatusNEW = subTasks.get(integer).getStatus() == "NEW";
                boolean isStatusDone = subTasks.get(integer).getStatus() == "DONE";

                if (isStatusNEW) {
                    countNew++;
                    continue;
                } else if (isStatusDone) {
                    countDone++;
                    continue;
                }
            }
        }
        if (countNew == epic.getSubsId().size()) {
            epicStatus = "NEW";
            epic.setStatus(epicStatus);
        } else if (countDone == epic.getSubsId().size()) {
            epicStatus = "DONE";
            epic.setStatus(epicStatus);
        } else {
            epicStatus = "IN PROGRESS";
            epic.setStatus(epicStatus);
        }
        return epic.getStatus();
    }

    public void updateSimpleTask(SimpleTask task) {
        simpleTask.remove(task.getId());
        simpleTask.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
       //int id = epic.getId();
        //epic.setSubsId(epics.get(id).getSubsId());
        //epics.remove(epic.getId());
       // epic.setSubsId(epic.getSubsId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);

    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epicTask = epics.get(subTask.getEpicId());
        updateEpicStatus(epicTask);
    }

    public void deleteSimpleTaskById(int id) {
        simpleTask.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer integer : epic.getSubsId()) {
            subTasks.remove(integer);
        }
        epic.getSubsId().clear();
        epics.remove(id);
    }

    public void deleteSubTaskById(int id) {
        int epId = subTasks.get(id).getEpicId();
        epics.get(epId).getSubsId().remove(Integer.valueOf(id));
        subTasks.remove(id);
        updateEpicStatus(epics.get(epId));
    }

    public ArrayList<SubTask> getListOfSubsOfEpic(Epic epic) {
        ArrayList<SubTask> listOfSubs = new ArrayList<>();
        for (Integer integer : epic.getSubsId()) {
            listOfSubs.add(subTasks.get(integer));
        }
        return listOfSubs;
    }
}