package service;

import com.google.gson.Gson;

public class HttpTaskManager extends FileBackedTasksManager{
    KVTaskClient client;
    Gson gson;
    public HttpTaskManager(String url) {
        gson = new Gson();
        client = new KVTaskClient(url);
    }


    @Override
    protected void save() {
    }
}
