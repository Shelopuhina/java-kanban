package service;

import model.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager, Comparator<Task>{
    final private Map<Integer, SimpleTask> simpleTask = new HashMap<>();
    final private Map<Integer, Epic> epics = new HashMap<>();
    final private Map<Integer, SubTask> subTasks = new HashMap<>();                //пробовала вставить лямбду самыми разными методами, но ничего не вышло:(
    final private Set<Task> prioritizedTasks = new TreeSet<Task>(this ::compare);//(task1,task2) -> task1.getStartTime().compareTo(task2.getStartTime())
    protected int nextId = 1;                                       // Comparator.comparing(Task::task.getStartTime());
    final private HistoryManager managerHistory = Managers.getDefaultHistory();
    //Comparator<Task> byStartTime = (task1,task2) -> (task1.getStartTime().compareTo(task2.getStartTime()));

    @Override
    public int compare(Task task1, Task task2) {
        return task1.getStartTime().compareTo(task2.getStartTime());

}

    public int getNextId() {
        return nextId;
    }

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
        List<Task> priorTasks = new ArrayList<>(prioritizedTasks);
        for (Task prioritizedTask : priorTasks) {
            if(prioritizedTask.getType().equals(TaskType.SIMPLE_TASK)) {
                prioritizedTasks.remove(prioritizedTask);
            }
        }
        for (Task task : managerHistory.getHistory()) {
            if(task.getType().equals(TaskType.SIMPLE_TASK)) {
                managerHistory.remove(task.getId());
            }
        }
    }

    @Override
    public void deleteAllEpics() throws IOException {
        epics.clear();
        subTasks.clear();
        for (Task task : managerHistory.getHistory()) {//эпики мы вроде в сортированный списко по заданию не кладем, так что и не удаляю..
            if (task.getType().equals(TaskType.EPIC)) {
                managerHistory.remove(task.getId());
            }
        }
    }

    @Override
    public void deleteAllSubTasks() throws IOException {

        for(Epic epic : epics.values()) {
            epic.getSubsId().clear();
            updateEpicStatus(epic);
            setEpicDuration(epic);
        }
        subTasks.clear();
        List<Task> priorTasks = new ArrayList<>(prioritizedTasks);
        for (Task prioritizedTask : priorTasks) {
            if (prioritizedTask.getType().equals(TaskType.SUBTASK)) {
                prioritizedTasks.remove(prioritizedTask);
            }
        }
        for (Task task : managerHistory.getHistory()) {
            if (task.getType().equals(TaskType.SUBTASK)) {
                managerHistory.remove(task.getId());
            }
        }
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
        findIfIsIntersection();
        prioritizedTasks.add(task);
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
        updateEpicStatus(epics.get(epId));
        setEpicDuration(epics.get(epId));
        findIfIsIntersection();
        prioritizedTasks.add(subTask);


        return subTask.getId();
    }
    public void setEpicDuration(Epic epic) {
        List<SubTask> subsID = getListOfSubsOfEpic(epic);
        if(subsID.size() == 0) {
            epic.setDuration(0);
            epic.setStartTime(null);
            epic.setEndTime(null);
        }else{
            Instant startTime = subsID.get(0).getStartTime();
            Instant endTime = subsID.get(0).getEndTime();
            for (SubTask subTask : subsID) {
                if(subTask.getStartTime().isBefore(startTime)) {
                    startTime = subTask.getStartTime();
                }
                if(subTask.getEndTime().isAfter(endTime)) {
                    endTime = subTask.getEndTime();
                }
            }
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(Duration.between(startTime,endTime).toMinutes());
        }
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
        findIfIsIntersection();
        prioritizedTasks.add(task);

    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        setEpicDuration(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {
        subTasks.put(subTask.getId(), subTask);
        Epic epicTask = epics.get(subTask.getEpicId());
        updateEpicStatus(epicTask);
        setEpicDuration(epicTask);
        findIfIsIntersection();
        prioritizedTasks.add(subTask);

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
        setEpicDuration(epics.get(epId));
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

    @Override
    public void  findIfIsIntersection() throws IOException{
        for (int i = 1; i < prioritizedTasks.size(); i++) {
            Task task = getPrioritizedTasks().get(i);
            if(task.getStartTime().isBefore(getPrioritizedTasks().get(i-1).getEndTime())) {
                throw new TasksIntersectionException("Обнаружено пересечние интервалов по времени междузадачами:\n"+
                        task.getName()+"-"+getPrioritizedTasks().get(i-1).getName());
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() throws IOException {
        return new ArrayList<>(prioritizedTasks);
    }

}

