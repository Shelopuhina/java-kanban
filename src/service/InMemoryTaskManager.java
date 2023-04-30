package service;

import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager{
    private Map<Integer, SimpleTask> simpleTask = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private int nextId = 1;
    private int nextIdEpic = 1;
    private int nextIdSub = 1;

   private HistoryManager managerHistory = Managers.getDefaultHistory();

    @Override
    public ArrayList<SimpleTask> getListOfSimpleTasks() {
        return new ArrayList<>(simpleTask.values());

    }

    @Override
    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getListOfsubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSimpleTasks() {
        simpleTask.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {

        for(Epic epic : epics.values()) {
            epic.getSubsId().clear();
            updateEpicStatus(epic);
        }
        subTasks.clear();
    }

    @Override
    public SimpleTask getTaskById(int id) {
        managerHistory.addTask(simpleTask.get(id));
        return simpleTask.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        managerHistory.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        managerHistory.addTask(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public int addSimpleTask(SimpleTask task) {
        task.setId(nextId);
        nextId++;
        simpleTask.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(nextIdEpic);
        nextIdEpic++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int addSubTask(SubTask subTask) {
        subTask.setId(nextIdSub);
        nextIdSub++;
        subTasks.put(subTask.getId(), subTask);
        int epId = subTask.getEpicId();
        epics.get(epId).getSubsId().add(subTask.getId());
        updateEpicStatus(epics.get(subTask.getEpicId()));
        return subTask.getId();
    }

    private EpicStatus updateEpicStatus(Epic epic) {
        EpicStatus epicStatus = null;
        int countNew = 0;
        int countDone = 0;
        for (Integer integer : epic.getSubsId()) {
            if (epic.getSubsId().isEmpty()) {
                epicStatus = EpicStatus.NEW;
            } else {
                boolean isStatusNEW = subTasks.get(integer).getStatus() == EpicStatus.NEW;
                boolean isStatusDone = subTasks.get(integer).getStatus() == EpicStatus.DONE;

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
            epicStatus = EpicStatus.NEW;
            epic.setStatus(epicStatus);
        } else if (countDone == epic.getSubsId().size()) {
            epicStatus = EpicStatus.DONE;
            epic.setStatus(epicStatus);
        } else {
            epicStatus = EpicStatus.IN_PROGRESS;
            epic.setStatus(epicStatus);
        }
        return epic.getStatus();
    }

    @Override
    public void updateSimpleTask(SimpleTask task) {
        simpleTask.remove(task.getId());
        simpleTask.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        //int id = epic.getId();
        //epic.setSubsId(epics.get(id).getSubsId());
        //epics.remove(epic.getId());
        // epic.setSubsId(epic.getSubsId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epicTask = epics.get(subTask.getEpicId());
        updateEpicStatus(epicTask);
    }

    @Override
    public void deleteSimpleTaskById(int id) {
        simpleTask.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer integer : epic.getSubsId()) {
            subTasks.remove(integer);
        }
        epic.getSubsId().clear();
        epics.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        int epId = subTasks.get(id).getEpicId();
        epics.get(epId).getSubsId().remove(Integer.valueOf(id));
        subTasks.remove(id);
        updateEpicStatus(epics.get(epId));
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
    public List<Task> getHistory() {
        return managerHistory.getHistory();
    }

}

