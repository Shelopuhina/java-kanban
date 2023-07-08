import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import service.TaskManager;
import utils.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private static HttpTaskServer server;
    private final Gson gson = Managers.getGson();
    private static TaskManager taskManager;
    private static SimpleTask task;
    private static Epic epic;
    private static SubTask subtask;


    @BeforeEach
    void init() throws IOException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer(taskManager);
        task = new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW, 100, Instant.ofEpochSecond(1000));
        epic = new Epic("сдать экзамен", "сессия в мае ", 0);
        subtask = new SubTask("купить учебник", " в буквоеде скидки ", 0, TaskStatus.NEW,
                100, Instant.ofEpochSecond(16000), 2);
        taskManager.addSimpleTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(3);
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test

    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type prioritizedTasksType = new TypeToken<List<Task>>() {}.getType();
        List<Task> list = gson.fromJson(response.body(), prioritizedTasksType);


        Assertions.assertAll("Проверка сортированного списка: ",
                () -> assertEquals(200, response.statusCode()),
                () -> assertNotNull(list, "Список отсортированных задач не возвращается."),
                () -> assertEquals(taskManager.getPrioritizedTasks().size(), list.size()));
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type historyType = new TypeToken<List<Task>>() {}.getType();
        List<Task> list = gson.fromJson(response.body(), historyType);


        Assertions.assertAll("Проверка истории: ",
                () -> assertEquals(200, response.statusCode()),
                () -> assertNotNull(list, "История не возвращается."),
                () -> assertEquals(taskManager.getHistory().size(), list.size()));
    }
    @Test
    public void getListOfSubsOfEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = taskManager.getEpics().get(2);
        Type subsType = new TypeToken<List<SubTask>>() {}.getType();
        List<Task> list = gson.fromJson(response.body(), subsType);


        Assertions.assertAll("Проверка списка сабтаска эпика: ",
                () -> assertEquals(200, response.statusCode()),
                () -> assertNotNull(list, "Список сабтасков эпика не возвращается."),
                () -> assertEquals(taskManager.getListOfSubsOfEpic(epic).size(), list.size()));
    }
    @Test
    public void getListOfSimpleTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type SimpleTaskType = new TypeToken<List<SimpleTask>>() {}.getType();
        List<Task> list = gson.fromJson(response.body(), SimpleTaskType);


        Assertions.assertAll("Проверка списка задач : ",
                () -> assertEquals(200, response.statusCode()),
                () -> assertNotNull(list, "Список задач не возвращается."),
                () -> assertEquals(taskManager.getListOfSimpleTasks().size(), list.size()));
    }
    @Test
    public void getListOfEpicsTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type epicType = new TypeToken<List<Epic>>() {}.getType();
        List<Task> list = gson.fromJson(response.body(), epicType);


        Assertions.assertAll("Проверка списка эпиков : ",
                () -> assertEquals(200, response.statusCode()),
                () -> assertNotNull(list, "Список эпиков не возвращается."),
                () -> assertEquals(taskManager.getListOfEpics().size(), list.size()));
    }
    @Test
    public void getListOfSubsTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type subsType = new TypeToken<List<SubTask>>() {}.getType();
        List<Task> list = gson.fromJson(response.body(), subsType);

        Assertions.assertAll("Проверка списка сабтаска эпика: ",
                () -> assertEquals(200, response.statusCode()),
                () -> assertNotNull(list, "Список сабтасков эпика не возвращается."),
                () -> assertEquals(taskManager.getListOfsubTasks().size(), list.size()));
    }
    @Test
    public void getSimpleTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type SimpleTaskType = new TypeToken<SimpleTask>() {}.getType();
        SimpleTask obj = gson.fromJson(response.body(),SimpleTaskType );

        Assertions.assertAll("Проверка получения задачи по id: ",
                () -> assertEquals(200, response.statusCode()),
                () -> assertNotNull(obj, "Задача не возвращается."));
    }
}
