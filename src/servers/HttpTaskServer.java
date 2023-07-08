package servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import utils.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static int responseCode = 400;
    private static byte[] response = new byte[0];
    private static TaskManager manager;
    private static Gson gson;
    private static HttpServer httpServer;
    private static HttpExchange httpExchange;
    private static String path;
    private static URI requestURI;

    public HttpTaskServer() throws IOException {
        this.manager = Managers.getDefault();
        this.httpServer = HttpServer.create();
        this.gson = Managers.getGson();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }
    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        this.httpServer = HttpServer.create();
        this.gson = Managers.getGson();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("http://localhost:"+PORT+"/tasks");
        httpServer.start();

    }
    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту: "+PORT);
    }
     static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            httpExchange = exchange;

            String method = exchange.getRequestMethod();
            requestURI = exchange.getRequestURI();
            path = requestURI.getPath();
            System.out.println("Путь: " + path);
            String[] splitStrings = path.split("/");
            System.out.println(splitStrings.length);


           try {
               switch (method) {
                   case "GET": {
                       if (path.endsWith("/tasks/")) {
                           getPrioritizedTasks();
                           break;
                       }
                       if (splitStrings[2].equals("task") && path.endsWith("task")) {
                           getAllSimpleTasks();
                           break;
                       }
                       if (splitStrings[2].equals("task") && (requestURI.getQuery().contains("id"))) {
                           getSimpleTaskByID();
                           break;
                       }
                       if (splitStrings[2].equals("epic") && path.endsWith("epic")) {
                           getAllEpics();
                           break;
                       }
                       if (splitStrings[2].equals("epic")&& (requestURI.getQuery().contains("id"))) {
                           getEpicById();
                           break;
                       }
                       if (splitStrings[2].equals("subtask") && path.endsWith("subtask")) {
                           getAllSubTasks();
                           break;
                       }
                       if (splitStrings[2].equals("subtask") && (!path.contains("epic")) && (requestURI.getQuery().contains("id"))) {
                           getSubTaskById();
                           break;
                       }
                       if (splitStrings[2].equals("subtask") && path.contains("epic")) {
                           getListOfSubsOfEpics();
                           break;
                       }
                       if (splitStrings[2].equals("history")) {
                           getHistory();
                           break;
                       }

                   }
                   case "POST": {
                       if (splitStrings[2].equals("task") && path.endsWith("task")) {
                           handlePostSimpleTask();
                       }
                       if (splitStrings[2].equals("epic") && path.endsWith("epic")) {
                           handlePostEpic();
                       }
                       if (splitStrings[2].equals("subtask") && path.endsWith("subtask")) {
                           handlePostSubTask();
                       }
                       break;
                   }
                   case "DELETE": {
                       if (splitStrings.length >= 3  && (requestURI.getQuery().contains("id"))) {
                           deleteAnyTaskById();
                       }
                       if (splitStrings[2].equals("task") && (!requestURI.getQuery().contains("id"))) {
                           deleteAllSimpleTasks();
                       }
                       if (splitStrings[2].equals("epic") && path.endsWith("epic")) {
                           deleteAllEpics();
                       }
                       if (splitStrings[2].equals("subtask") && path.endsWith("subtask")) {
                           deleteAllSubTasks();
                       }

                       break;
                   }
                   default:
                       System.out.println("Метод " + method + "не поддерживается");
               }
               Headers headers = httpExchange.getResponseHeaders();
               headers.set("Content-type","application/json");
               headers.set("Accept", "application/json");
               exchange.sendResponseHeaders(responseCode, response.length);
               try (OutputStream os = exchange.getResponseBody()) {
                   os.write(response);
               }
           }catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
               System.out.println("Во время выполнения запроса ресурса возникла ошибка.\n" +
                       "Проверьте, пожалуйста, адрес и повторите попытку.");

           }
            exchange.close();

        }
        private void getAllSimpleTasks() throws IOException, InterruptedException {
            System.out.println("Началась обработка /tasks/task/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            List<SimpleTask> tasks = manager.getListOfSimpleTasks();
            String jsonList = gson.toJson(tasks);


            if (!jsonList.isEmpty()) {
                response = jsonList.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void getAllEpics() throws IOException, InterruptedException {
            System.out.println("Началась обработка /tasks/epic/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/epic/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            List<Epic> epics = manager.getListOfEpics();
            String jsonList = gson.toJson(epics);

            if (!jsonList.isEmpty()) {
                response = jsonList.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }

        }
        private void getAllSubTasks() throws IOException, InterruptedException {
            System.out.println("Началась обработка /tasks/subtask/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtask/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            List<SubTask> subtasks = manager.getListOfsubTasks();
            String jsonList = gson.toJson(subtasks);


            if (!jsonList.isEmpty()) {
                response = jsonList.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void getSimpleTaskByID() throws IOException, InterruptedException {
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/task/?id="+id+"запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/?id="+id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            SimpleTask task = manager.getTaskById(id);
            String jsonString = gson.toJson(task);

            if (!jsonString.isEmpty()) {
                response = jsonString.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void getEpicById() throws IOException {
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/epic/?id="+id+"запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/epic/?id="+id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            Epic epic = manager.getEpicById(id);
            String jsonString = gson.toJson(epic);

            if (!jsonString.isEmpty()) {
                response = jsonString.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void getSubTaskById() throws IOException {
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/subtask/?id="+id+"запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtask/?id="+id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            SubTask sub = manager.getSubTaskById(id);
            String jsonString = gson.toJson(sub);

            if (!jsonString.isEmpty()) {
                response = jsonString.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void getListOfSubsOfEpics() {
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/subtask/epic/?id="+id+"запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id="+id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            Epic epic = manager.getEpics().get(id);
            List<SubTask> subsId = manager.getListOfSubsOfEpic(epic);
            String jsonList = gson.toJson(subsId);


            if (!jsonList.isEmpty()) {
                response = jsonList.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void getHistory() throws IOException {
            System.out.println("Началась обработка /tasks/history/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/history/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            List<Task> tasks = manager.getHistory();
            String jsonList = gson.toJson(tasks);


            if (!jsonList.isEmpty()) {
                response = jsonList.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void getPrioritizedTasks() throws IOException {
            System.out.println("Началась обработка /tasks/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

            List<Task> tasks = manager.getPrioritizedTasks();
            String jsonList = gson.toJson(tasks);


            if (!jsonList.isEmpty()) {
                response = jsonList.getBytes(DEFAULT_CHARSET);
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void handlePostSimpleTask() throws IOException {
            System.out.println("POST: началась обработка /tasks/task запроса от клиента.\n");

            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            SimpleTask task = gson.fromJson(body, SimpleTask.class);
            responseCode = 200;

            if (!manager.getSimpleTask().containsKey(task.getId())) {
                manager.addSimpleTask(task);
                String newResponse = "Задача " + task.getName() +" добавлена " + "\n" + body;
                response = newResponse.getBytes(DEFAULT_CHARSET);
            } else {
                manager.updateSimpleTask(task);
                String newResponse = "Задача " + task.getName() +" обновлена " + "\n" + body;
                response = newResponse.getBytes(DEFAULT_CHARSET);
            }
        }
        private void handlePostEpic() throws IOException {
            System.out.println("POST: началась обработка /tasks/epic запроса от клиента.\n");

            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);
            responseCode = 200;

            if (!manager.getEpics().containsKey(epic.getId())) {
                manager.addEpic(epic);
                System.out.println("Добавлен новый эпик " + epic.getName() + "\n" + body);
            } else {
                manager.updateEpic(epic);
                System.out.println("Эпик с id=" + epic.getId() + " обновлен.\n" + body);
            }
        }
        private void handlePostSubTask() throws IOException {
            System.out.println("POST: началась обработка /tasks/subtask запроса от клиента.\n");

            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            SubTask subtask = gson.fromJson(body, SubTask.class);
            responseCode = 200;

            if (!manager.getSubTasks().containsKey(subtask.getId())) {
                manager.addSubTask(subtask);
                System.out.println("Добавлен новый сабтаск " + subtask.getName() + "\n" + body);
            } else {
                manager.updateSubTask(subtask);
                System.out.println("Сабтаск с id=" + subtask.getId() + " обновлен.\n" + body);
            }
        }
        private void deleteAllSimpleTasks() throws IOException {
            System.out.println("Началась обработка /tasks/task/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

            manager.deleteAllSimpleTasks();

            if (manager.getSimpleTask().isEmpty()) {
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void deleteAllEpics() throws IOException {
            System.out.println("Началась обработка /tasks/epic/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/epic/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

            manager.deleteAllEpics();

            if (manager.getEpics().isEmpty()) {
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void deleteAllSubTasks() throws IOException {
            System.out.println("Началась обработка /tasks/subtask/ запроса от клиента.");

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtask/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

            manager.deleteAllSubTasks();

            if (manager.getSubTasks().isEmpty()) {
                responseCode = 200;
            }else{
                responseCode = 400;
            }
        }
        private void deleteAnyTaskById() throws IOException {
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            if (manager.getSimpleTask().containsKey(id)) {
                System.out.println("Началась обработка /tasks/task/?id="+id+"запроса от клиента.");

                HttpClient client = HttpClient.newHttpClient();
                URI url = URI.create("http://localhost:8080/tasks/task/?id="+id);
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
                manager.deleteSimpleTaskById(id);
                System.out.println("Задача удалена");
            }
            if (manager.getEpics().containsKey(id)) {
                System.out.println("Началась обработка /tasks/epic/?id="+id+"запроса от клиента.");

                HttpClient client = HttpClient.newHttpClient();
                URI url = URI.create("http://localhost:8080/tasks/epic/?id="+id);
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
                manager.deleteEpicById(id);
                System.out.println("Эпик удален");
            }
            if (manager.getSubTasks().containsKey(id)) {
                System.out.println("Началась обработка /tasks/subtask/?id=" + id + "запроса от клиента.");

                HttpClient client = HttpClient.newHttpClient();
                URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + id);
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

                manager.deleteSubTaskById(id);
                System.out.println("Сабтаск удален");
            }
                responseCode = 200;
        }
        }

    public static void main(String[] args) throws IOException {

        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();

       SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW,100, Instant.ofEpochSecond(1000));
        manager.addSimpleTask(task1);
        SimpleTask task2 = new SimpleTask("Собрать коробки2", "Для переезда2", 0, TaskStatus.DONE,100, Instant.ofEpochSecond(80000));
        manager.addSimpleTask(task2);


        Epic epic1 = new Epic("сдать экзамен", "сессия в мае ",0);
        manager.addEpic(epic1);

        SubTask subTask1 = new SubTask("купить учебник", " в буквоеде скидки ", 0, TaskStatus.NEW,
                100, Instant.ofEpochSecond(16000),epic1.getId());
        manager.addSubTask(subTask1);
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask1.getId());

    }
    }
