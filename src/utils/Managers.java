package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.*;

import java.time.Instant;

public class Managers {
    public static TaskManager getDefault() {
        HttpTaskManager manager = new HttpTaskManager("http://localhost:8078");
        return manager;
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
   public static FileBackedTasksManager getDefaultFileBackedManager() {
       return new FileBackedTasksManager();
   }
   public static Gson getGson() {
       GsonBuilder gsonBuilder = new GsonBuilder();
       gsonBuilder.serializeNulls();
       gsonBuilder.registerTypeAdapter(Instant.class,new InstantAdapter());
        return gsonBuilder.create();
   }
}
