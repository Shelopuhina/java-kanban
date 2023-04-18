package service;

import model.Epic;
import model.SimpleTask;
import model.SubEpic;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, SimpleTask> simpleTask = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubEpic> subEpics = new HashMap<>();
    private int nextId = 1;
    private int nextIdEpic = 1;
    private int nextIdSub = 1;


    public ArrayList<SimpleTask> getListOfSimpleTasks() {
        return new ArrayList<>(simpleTask.values());

    }

    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubEpic> getListOfSubEpics() {
        return new ArrayList<>(subEpics.values());
    }

    public void deleteAllSimpleTasks() {
        simpleTask.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subEpics.clear();
    }

    public void deleteAllSubEpics() {

        for (Integer id : subEpics.keySet()) {
            int epicId = subEpics.get(id).getEpicId();
            if (!epics.get(epicId).getSubsId().isEmpty()) {
                epics.get(epicId).getSubsId().clear();
            }
            getEpicStatus(epics.get(epicId));
        }
    }

    public SimpleTask getTaskById(int id) {
        return simpleTask.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubEpic getSubEpicById(int id) {
        return subEpics.get(id);
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

    public int addSubEpic(SubEpic subEpic) {
        subEpic.setId(nextIdSub);
        nextIdSub++;
        subEpics.put(subEpic.getId(), subEpic);
        int epId = subEpic.getEpicId();
        epics.get(epId).getSubsId().add(subEpic.getId());
        getEpicStatus(epics.get(subEpic.getEpicId()));
        return subEpic.getId();
    }

    private String getEpicStatus(Epic epic) {
        String epicStatus = null;
        int countNew = 0;
        int countDone = 0;
        for (Integer integer : epic.getSubsId()) {
            if (epic.getSubsId().isEmpty()) {
                epicStatus = "NEW";
            } else {
                boolean isStatusNEW = subEpics.get(integer).getStatus() == "NEW";
                boolean isStatusDone = subEpics.get(integer).getStatus() == "DONE";

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
       int id = epic.getId();
        epic.setSubsId(epics.get(id).getSubsId());
        epics.remove(epic.getId());
        epic.setSubsId(epic.getSubsId());
        epics.put(epic.getId(), epic);
        getEpicStatus(epic);

    }

    public void updateSubEpic(SubEpic subEpic) {
        subEpics.remove(subEpic.getId());
        subEpics.put(subEpic.getId(), subEpic);
        Epic epicTask = epics.get(subEpic.getEpicId());
        getEpicStatus(epicTask);
    }

    public void deleteSimpleTaskById(int id) {
        simpleTask.remove(id);
    }

    public void deleteEpicById(int id) {
        for (Integer integer : epics.get(id).getSubsId()) {
            subEpics.remove(integer);
        }
        epics.get(id).getSubsId().clear();
    }

    public void deleteSubEpicById(int id) {
        int epId = subEpics.get(id).getEpicId();
        epics.get(epId).getSubsId().remove(id);
        subEpics.remove(id);
        getEpicStatus(epics.get(epId));
    }

    public ArrayList<SubEpic> getListOfSubsOfEpic(Epic epic) {
        ArrayList<SubEpic> listOfSubs = new ArrayList<>();
        for (Integer integer : epic.getSubsId()) {
            listOfSubs.add(subEpics.get(integer));
        }
        return listOfSubs;
    }
}