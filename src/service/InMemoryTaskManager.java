package service;

import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager{
    private Map<Integer, SimpleTask> simpleTask = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public int getNextId() {
        return nextId;
    }

    protected int nextId = 1;
    private HistoryManager managerHistory = Managers.getDefaultHistory();

    public HistoryManager getManagerHistory() {
        return managerHistory;
    }

    @Override
    public List<SimpleTask> getListOfSimpleTasks() {
        return new ArrayList<>(simpleTask.values());
    }

    public Map<Integer, SimpleTask> getSimpleTask() {
        return simpleTask;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public List<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getListOfsubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSimpleTasks() throws IOException {
        simpleTask.clear();
    }

    @Override
    public void deleteAllEpics() throws IOException {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() throws IOException {

        for(Epic epic : epics.values()) {
            epic.getSubsId().clear();
            updateEpicStatus(epic);
        }
        subTasks.clear();
    }

    @Override
    public SimpleTask getTaskById(int id) throws IOException {
        managerHistory.addTask(simpleTask.get(id));
        return simpleTask.get(id);
    }

    @Override
    public Epic getEpicById(int id) throws IOException {
        managerHistory.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) throws IOException {
             managerHistory.addTask(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public int addSimpleTask(SimpleTask task) throws IOException {
        task.setId(nextId);
        nextId++;
        simpleTask.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) throws IOException {
        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int addSubTask(SubTask subTask) throws IOException {
        subTask.setId(nextId);
        nextId++;
        subTasks.put(subTask.getId(), subTask);
        int epId = subTask.getEpicId();
        epics.get(epId).getSubsId().add(subTask.getId());
        updateEpicStatus(epics.get(subTask.getEpicId()));
        return subTask.getId();
    }

    public TaskStatus updateEpicStatus(Epic epic) {
        TaskStatus epicStatus = null;
        int countNew = 0;
        int countDone = 0;

        for (Integer integer : epic.getSubsId()) {
            if (epic.getSubsId().isEmpty()) {
                epicStatus = TaskStatus.NEW;
            } else {
                boolean isStatusNEW = subTasks.get(integer).getStatus() == TaskStatus.NEW;
                boolean isStatusDone = subTasks.get(integer).getStatus() == TaskStatus.DONE;
                boolean isStatusInProgress = subTasks.get(integer).getStatus() == TaskStatus.IN_PROGRESS;


                if (isStatusNEW) {
                    countNew++;
                } else if (isStatusDone) {
                    countDone++;
                } else if(isStatusInProgress) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    epicStatus = TaskStatus.IN_PROGRESS;
                }
            }
        }

        if (countNew == epic.getSubsId().size()) {
            epicStatus = TaskStatus.NEW;
            epic.setStatus(epicStatus);
        } else if (countDone == epic.getSubsId().size()) {
            epicStatus = TaskStatus.DONE;
            epic.setStatus(epicStatus);
        } else {
            epicStatus = TaskStatus.IN_PROGRESS;
            epic.setStatus(epicStatus);
        }
        return epic.getStatus();
    }

    @Override
    public void updateSimpleTask(SimpleTask task) throws IOException {
        simpleTask.remove(task.getId());
        simpleTask.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {
        subTasks.put(subTask.getId(), subTask);
        Epic epicTask = epics.get(subTask.getEpicId());
        updateEpicStatus(epicTask);
    }

    @Override
    public void deleteSimpleTaskById(int id) throws IOException {
        simpleTask.remove(id);
        managerHistory.remove(id);
    }

    @Override
    public void deleteEpicById(int id) throws IOException {
        Epic epic = epics.get(id);
        for (Integer integer : epic.getSubsId()) {
            managerHistory.remove(integer);
            subTasks.remove(integer);
        }
        epic.getSubsId().clear();
        epics.remove(id);
        managerHistory.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) throws IOException {
        int epId = subTasks.get(id).getEpicId();
        epics.get(epId).getSubsId().remove(Integer.valueOf(id));
        subTasks.remove(id);
        updateEpicStatus(epics.get(epId));
        managerHistory.remove(id);
    }

    @Override
    public ArrayList<SubTask> getListOfSubsOfEpic(Epic epic) {
        ArrayList<SubTask> listOfSubs = new ArrayList<>();
        for (Integer integer : epic.getSubsId()) {
            listOfSubs.add(subTasks.get(integer));
        }
        return listOfSubs;
    }

    @Override
    public List<Task> getHistory() throws IOException {
        return managerHistory.getHistory();
    }

}

