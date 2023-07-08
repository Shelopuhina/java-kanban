package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;
import utils.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager{
   protected KVTaskClient client;
    private static Gson gson;

    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    public KVTaskClient getClient() {
        return client;
    }

    @Override
    protected void save() throws IOException {
        String tasks = gson.toJson(getSimpleTask());
        client.put("tasks/task", tasks);

        String epics = gson.toJson(getEpics());
        client.put("tasks/epic", epics);
        String subtasks = gson.toJson(getSubTasks());
        client.put("tasks/subtask", subtasks);
        String prioritizedTasks = gson.toJson(getPrioritizedTasks());
        client.put("tasks", prioritizedTasks);
        String history = gson.toJson(getHistory());
        client.put("tasks/history", history);
    }

    public void load() throws IOException {
        String jsonTasks = getClient().load("tasks/task");
        Type taskType = new TypeToken<Map<Integer, SimpleTask>>(){}.getType();
        Map<Integer, SimpleTask> tasks = gson.fromJson(jsonTasks, taskType);
        getSimpleTask().putAll(tasks);

        String jsonEpics = getClient().load("tasks/epic");
        Type epicType = new TypeToken<Map<Integer, Epic>>(){}.getType();
        Map<Integer, Epic> epics = gson.fromJson(jsonEpics, epicType);
        getEpics().putAll(epics);

        String jsonSubTasks = getClient().load("tasks/subtask");
        Type subtaskType = new TypeToken<Map<Integer, Epic>>(){}.getType();
        Map<Integer, SubTask> subtasks = gson.fromJson(jsonSubTasks, subtaskType);
        getSubTasks().putAll(subtasks);

        String jsonPrioritizedTasks = getClient().load("tasks");
        Type prioritizedTasksType = new TypeToken<List<Task>>(){}.getType();
        List<Task> prioritizedTasks = gson.fromJson(jsonPrioritizedTasks, prioritizedTasksType);
        getPrioritizedTasks().addAll(prioritizedTasks);

        String jsonHistory = getClient().load("tasks/history");
        Type historyType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(jsonHistory, historyType);
        getHistory().addAll(history);



    }
}
