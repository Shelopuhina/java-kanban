package service;

import Exceptions.ManagerSaveException;
import model.*;
import model.enums.TaskStatus;
import model.enums.TaskType;
import servers.KVServer;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager  extends InMemoryTaskManager implements  TaskManager  {
    private static File newFile = new File("C:\\Users\\T-3000\\dev\\java-kanban\\src\\service", "fileForData.txt");




    protected void save() throws IOException {
        try (Writer file = new FileWriter(newFile.getName())){
            file.write("id,type,name,status,description,duration,startTime,endTime,epic\n");
            for (SimpleTask simpleTask : getSimpleTask().values()) {
                file.write(toString(simpleTask));
            }
            for (Epic epic : getEpics().values()) {
                file.write(toString(epic));
            }
            for (SubTask subTask : getSubTasks().values()) {
                file.write(toString(subTask));
            }
            file.write("\n");
            file.write(historyToString(super.getManagerHistory()));
        } catch (IOException exc) {
            throw new ManagerSaveException("Ошибка сохраенения в файл");
        }
    }
    public FileBackedTasksManager  loadFromFile(File file) {
        FileBackedTasksManager newmanager = new FileBackedTasksManager();
        try  (FileReader reader = new FileReader(file.getName())) {
            BufferedReader br = new BufferedReader(reader);
            List <String> allStrings = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                allStrings.add(line);
            }
            int maxNextId = 0;
            for (int i = 1; i < (allStrings.size()-1); i++) {
                if(!(allStrings.get(i).isEmpty())) {
                    Task newTask = newmanager.fromString(allStrings.get(i));
                    if (newTask.getId() > maxNextId) {
                        maxNextId = newTask.getId();
                    }
                    String[] split = allStrings.get(i).split(",");
                    int epicId = 0;
                    if (newTask.getType().equals(TaskType.SUBTASK)) {
                        epicId = Integer.parseInt(split[8]);
                        SubTask subTask = new SubTask(newTask.getName(), newTask.getDescription(), newTask.getId(),
                                newTask.getStatus(),newTask.getDuration(),newTask.getStartTime(),epicId);
                        newmanager.getSubTasks().put(subTask.getId(), subTask);
                        List<Integer> subsId = newmanager.getEpicById(epicId).getSubsId();
                        subsId.add(newTask.getId());
                    } else if (newTask.getType().equals(TaskType.EPIC)) {
                        Epic epic = new Epic(newTask.getName(), newTask.getDescription(), newTask.getId());
                        newmanager.getEpics().put(epic.getId(), epic);
                    } else {
                        SimpleTask simpleTask = new SimpleTask(newTask.getName(), newTask.getDescription(), newTask.getId(),
                                newTask.getStatus(),newTask.getDuration(),newTask.getStartTime());
                        newmanager.getSimpleTask().put(simpleTask.getId(), simpleTask);
                    }

                }else{
                    String nextString = allStrings.get(i+1);
                    List<Integer> newHistory = historyFromString(nextString);

                    for (Integer id : newHistory) {
                        if(newmanager.getSimpleTask().containsKey(id)) {
                            newmanager.getTaskById(id);
                        }else if(newmanager.getEpics().containsKey(id)) {
                            newmanager.getEpicById(id);
                        }else{
                            newmanager.getSubTaskById(id);
                        }
                    }

                }
            }
            newmanager.nextId = maxNextId + 1;
        } catch (IOException exc) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        return newmanager;
    }
    private String toString(Task task) {
        String taskToString = null;
        String taskToString2 = null;

        if(task.getType().equals(TaskType.SUBTASK)) {
            for (SubTask sub : getSubTasks().values()) {
                if (task.getId() == sub.getId()) {
                    taskToString = String.join(",", Integer.toString(sub.getId()), sub.getType().toString(), sub.getName(),
                            sub.getStatus().toString(), sub.getDescription(), String.valueOf(sub.getDuration()),
                            String.valueOf(sub.getStartTime()), String.valueOf(sub.getEndTime()), Integer.toString(sub.getEpicId()) );
                    taskToString2 = taskToString+"\n";//id,type,name,status,description,duration,startTime,endTime,epic
                }
            }
        } else if(task.getType().equals(TaskType.EPIC)) {
            Epic epic = (Epic) task;
            if(epic.getSubsId().isEmpty()) {
                taskToString = String.join(",", Integer.toString(task.getId()), task.getType().toString(), task.getName(),
                        task.getStatus().toString(), task.getDescription(),String.valueOf(task.getDuration()),
                        String.valueOf(Instant.ofEpochSecond(0)), String.valueOf(Instant.ofEpochSecond(0)));
            }else{
                taskToString = String.join(",", Integer.toString(task.getId()), task.getType().toString(), task.getName(),
                        task.getStatus().toString(), task.getDescription(),String.valueOf(task.getDuration()),
                        String.valueOf(task.getStartTime()), String.valueOf(task.getEndTime()));
            }
           taskToString2 = taskToString+"\n";
        }else{
            taskToString = String.join(",", Integer.toString(task.getId()), task.getType().toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(),String.valueOf(task.getDuration()),
                    String.valueOf(task.getStartTime()), String.valueOf(task.getEndTime()));
            taskToString2 = taskToString+"\n";
        }
        return taskToString2;
    }
    private Task fromString (String taskToString) {
        String[] taskInfo = taskToString.split(",");
        int epicId = 0;
        int id = Integer.parseInt(taskInfo[0]);
        String name = (taskInfo[2]);
        String description = (taskInfo[4]);
        Instant startTime = Instant.parse(taskInfo[6]);
        long duration = Long.parseLong(taskInfo[5]);
        TaskStatus status = (TaskStatus.valueOf(taskInfo[3].toUpperCase()));
        TaskType type = null;
        if(taskInfo[1].equals(TaskType.SIMPLE_TASK.toString())) {
             type = (TaskType.SIMPLE_TASK);
        } else if (taskInfo[1].equals(TaskType.EPIC.toString())) {
             type = (TaskType.EPIC);
        } else if(taskInfo[1].equals(TaskType.SUBTASK.toString())) {
             type = (TaskType.SUBTASK);
        }
        Task task = new Task(name, description,id, status,type, duration, startTime);
        return task;
    }
    private static String historyToString(HistoryManager manager) {
        String stringHistory = null;
        List<Task> history= new ArrayList<>(manager.getHistory());

        List<String> historyIdString= new ArrayList<>();
        if(!(manager.getHistory().isEmpty())) {
            for (int i = 0; i < history.size(); i++) {
                historyIdString.add(String.valueOf(history.get(i).getId()));
            }
            stringHistory = String.join(",",historyIdString);




        }else{
            stringHistory = "История просмотров пуста";
        }
               return stringHistory ;
    }
    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if(value.equals("История просмотров пуста")) {
            return history;
        } else {
        String[] split = value.split(",");
        for (int i = 0; i < split.length; i++) {
            history.add(Integer.valueOf(split[i]));
        }
        return  history;
    }}
    @Override
    public void deleteAllSimpleTasks() throws IOException {
        super.deleteAllSimpleTasks();
        save();
    }
    @Override
    public void deleteAllEpics() throws IOException {
        super.deleteAllEpics();
        save();

    }
    @Override
    public void deleteAllSubTasks() throws IOException {
        super.deleteAllSubTasks();
        save();

    }
    @Override
    public int addSimpleTask(SimpleTask task) throws IOException {
        super.addSimpleTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) throws IOException {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addSubTask(SubTask subTask) throws IOException {
        super.addSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void updateSimpleTask(SimpleTask task) throws IOException {
        super.updateSimpleTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        super.updateEpic(epic);
        save();

    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {
        super.updateSubTask(subTask);
        save();

    }

    @Override
    public void deleteSimpleTaskById(int id) throws IOException {
        super.deleteSimpleTaskById(id);
        save();

    }

    @Override
    public void deleteEpicById(int id) throws IOException {
        super.deleteEpicById(id);
        save();

    }

    @Override
    public void deleteSubTaskById(int id) throws IOException {
        super.deleteSubTaskById(id);
        save();
    }
    @Override
    public List<Task> getHistory() throws IOException {

        return super.getHistory();
    }

    @Override
    public SimpleTask getTaskById(int id) throws IOException {
        SimpleTask forReturn = super.getTaskById(id);
        save();
        return forReturn;
    }

    @Override
    public Epic getEpicById(int id) throws IOException {
        Epic forReturn = super.getEpicById(id);
        save();
        return forReturn;
    }

    @Override
    public SubTask getSubTaskById(int id) throws IOException {
        SubTask forReturn = super.getSubTaskById(id);
        save();
        return forReturn;
    }
}