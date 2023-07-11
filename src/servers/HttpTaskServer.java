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
import service.HttpTaskManager;
import service.TaskManager;
import utils.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final TaskManager manager;
    private final Gson gson;
    private final HttpServer httpServer;
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
      class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            final String method = exchange.getRequestMethod();
            final URI requestURI = exchange.getRequestURI();
            final String path = requestURI.getPath();
            System.out.println("Путь: " + path);
            String[] splitStrings = path.split("/");
            try {
               switch (method) {
                   case "GET": {
                       if (path.endsWith("/tasks/")) {
                           getPrioritizedTasks(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("task") && path.endsWith("task")) {
                           getAllSimpleTasks(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("task") && (requestURI.getQuery().contains("id"))) {
                           getSimpleTaskByID(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("epic") && path.endsWith("epic")) {
                           getAllEpics(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("epic")&& (requestURI.getQuery().contains("id"))) {
                           getEpicById(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("subtask") && path.endsWith("subtask")) {
                           getAllSubTasks(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("subtask") && (!path.contains("epic")) && (requestURI.getQuery().contains("id"))) {
                           getSubTaskById(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("subtask") && path.contains("epic")) {
                           getListOfSubsOfEpics(exchange);
                           break;
                       }
                       if (splitStrings[2].equals("history")) {
                           getHistory(exchange);
                           break;
                       }

                   }
                   case "POST": {
                       if (splitStrings[2].equals("task") && path.endsWith("task")) {
                           handlePostSimpleTask(exchange);
                       }
                       if (splitStrings[2].equals("epic") && path.endsWith("epic")) {
                           handlePostEpic(exchange);
                       }
                       if (splitStrings[2].equals("subtask") && path.endsWith("subtask")) {
                           handlePostSubTask(exchange);
                       }
                       break;
                   }
                   case "DELETE": {
                       if ((!(requestURI.getQuery() ==null)) && requestURI.getQuery().contains("id")) {
                           deleteAnyTaskById(exchange);
                       }
                       if (splitStrings[2].equals("task") && path.endsWith("task")) {
                           deleteAllSimpleTasks(exchange);
                       }
                       if (splitStrings[2].equals("epic") && path.endsWith("epic")) {
                           deleteAllEpics(exchange);
                       }
                       if (splitStrings[2].equals("subtask") && path.endsWith("subtask")) {
                           deleteAllSubTasks(exchange);
                       }

                       break;
                   }
                   default:
                       System.out.println("Метод " + method + "не поддерживается");
               }
               Headers headers = exchange.getResponseHeaders();
               headers.set("Accept", "application/json");
           }catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
               System.out.println("Во время выполнения запроса ресурса возникла ошибка.\n" +
                       "Проверьте, пожалуйста, адрес и повторите попытку.");
           }
            exchange.close();

        }
          private void sendText(HttpExchange h, String text) throws IOException {
                byte[] resp = text.getBytes(StandardCharsets.UTF_8);
                h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                h.sendResponseHeaders(200, resp.length);
                h.getResponseBody().write(resp);
          }
          private void getAllSimpleTasks(HttpExchange exchange) throws IOException, InterruptedException {
              System.out.println("Началась обработка /tasks/task/ запроса от клиента.");
              List<SimpleTask> tasks = manager.getListOfSimpleTasks();
              String jsonList = gson.toJson(tasks);

              if (!jsonList.isEmpty()) {
                  sendText(exchange, jsonList);
              }
          }
        private void getAllEpics(HttpExchange exchange) throws IOException, InterruptedException {
            System.out.println("Началась обработка /tasks/epic/ запроса от клиента.");
            List<Epic> epics = manager.getListOfEpics();
            String jsonList = gson.toJson(epics);

            if (!jsonList.isEmpty()) {
                sendText(exchange, jsonList);
            }
        }
        private void getAllSubTasks(HttpExchange exchange) throws IOException, InterruptedException {
            System.out.println("Началась обработка /tasks/subtask/ запроса от клиента.");
            List<SubTask> subtasks = manager.getListOfsubTasks();
            String jsonList = gson.toJson(subtasks);

            if (!jsonList.isEmpty()) {
                sendText(exchange, jsonList);
            }
        }
        private void getSimpleTaskByID(HttpExchange exchange) throws IOException, InterruptedException {
            URI requestURI = exchange.getRequestURI();
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/task/?id="+id+"запроса от клиента.");
            SimpleTask task = manager.getTaskById(id);
            String jsonString = gson.toJson(task);

            if (!jsonString.isEmpty()) {
                sendText(exchange, jsonString);
            }
        }
        private void getEpicById(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/epic/?id="+id+"запроса от клиента.");
            Epic epic = manager.getEpicById(id);
            String jsonString = gson.toJson(epic);

            if (!jsonString.isEmpty()) {
                sendText(exchange, jsonString);
            }
        }
        private void getSubTaskById(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/subtask/?id="+id+"запроса от клиента.");
            SubTask sub = manager.getSubTaskById(id);
            String jsonString = gson.toJson(sub);

            if (!jsonString.isEmpty()) {
                sendText(exchange, jsonString);
            }
        }
        private void getListOfSubsOfEpics(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            System.out.println("Началась обработка /tasks/subtask/epic/?id="+id+"запроса от клиента.");
            Epic epic = manager.getEpics().get(id);
            List<SubTask> subsId = manager.getListOfSubsOfEpic(epic);
            String jsonList = gson.toJson(subsId);

            if (!jsonList.isEmpty()) {
                sendText(exchange, jsonList);
            }
        }
        private void getHistory(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/history/ запроса от клиента.");
            List<Task> tasks = manager.getHistory();
            String jsonList = gson.toJson(tasks);

            if (!jsonList.isEmpty()) {
                sendText(exchange, jsonList);
            }
        }
        private void getPrioritizedTasks(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/ запроса от клиента.");
            List<Task> tasks = manager.getPrioritizedTasks();
            String jsonList = gson.toJson(tasks);

            if (!jsonList.isEmpty()) {
                sendText(exchange, jsonList);
            }
        }
        private void handlePostSimpleTask(HttpExchange exchange) throws IOException {
            System.out.println("POST: началась обработка /tasks/task запроса от клиента.\n");

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            SimpleTask task = gson.fromJson(body, SimpleTask.class);

            if (!manager.getSimpleTask().containsKey(task.getId())) {
                manager.addSimpleTask(task);
                String newResponse = "Задача " + task.getName() +" добавлена " + "\n" + body;
                sendText(exchange, newResponse);
            } else {
                manager.updateSimpleTask(task);
                String newResponse = "Задача " + task.getName() +" обновлена " + "\n" + body;
                sendText(exchange, newResponse);
            }
        }
        private void handlePostEpic(HttpExchange exchange) throws IOException {
            System.out.println("POST: началась обработка /tasks/epic запроса от клиента.\n");

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(),  StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);

            if (!manager.getEpics().containsKey(epic.getId())) {
                manager.addEpic(epic);
                String newResponse = "Добавлен новый эпик " + epic.getName() + "\n" + body;
                sendText(exchange, newResponse);
            } else {
                manager.updateEpic(epic);
                String newResponse = "Эпик с id=" + epic.getId() + " обновлен.\n" + body;
                sendText(exchange, newResponse);
            }
        }
        private void handlePostSubTask(HttpExchange exchange) throws IOException {
            System.out.println("POST: началась обработка /tasks/subtask запроса от клиента.\n");

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(),  StandardCharsets.UTF_8);
            SubTask subtask = gson.fromJson(body, SubTask.class);

            if (!manager.getSubTasks().containsKey(subtask.getId())) {
                manager.addSubTask(subtask);
                String newResponse = "Добавлен новый сабтаск " + subtask.getName() + "\n" + body;
                sendText(exchange, newResponse);
            } else {
                manager.updateSubTask(subtask);
                String newResponse = "Сабтаск с id=" + subtask.getId() + " обновлен.\n" + body;
                sendText(exchange, newResponse);
            }
        }
        private void deleteAllSimpleTasks(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/task/ запроса от клиента.");
            manager.deleteAllSimpleTasks();

            if (manager.getSimpleTask().isEmpty()) {
                System.out.println("Все задачи удалены.");
                byte[] resp = new byte[0];
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            }
        }
        private void deleteAllEpics(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/epic/ запроса от клиента.");
            manager.deleteAllEpics();

            if (manager.getEpics().isEmpty()) {
                System.out.println("Все эпики удалены.");
                byte[] resp = new byte[0];
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            }
        }
        private void deleteAllSubTasks(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/subtask/ запроса от клиента.");
            manager.deleteAllSubTasks();

            if (manager.getSubTasks().isEmpty()) {
                System.out.println("Все сабтаски удалены.");
                byte[] resp = new byte[0];
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            }
        }
        private void deleteAnyTaskById(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            String getId = requestURI.getQuery();
            int index = getId.indexOf("=");
            int id = Integer.parseInt(getId.substring(index+1));
            if (manager.getSimpleTask().containsKey(id)) {
                System.out.println("Началась обработка /tasks/task/?id="+id+"запроса от клиента.");

                manager.deleteSimpleTaskById(id);
                System.out.println("Задача удалена");
            }
            if (manager.getEpics().containsKey(id)) {
                System.out.println("Началась обработка /tasks/epic/?id="+id+"запроса от клиента.");

                manager.deleteEpicById(id);
                System.out.println("Эпик удален");
            }
            if (manager.getSubTasks().containsKey(id)) {
                System.out.println("Началась обработка /tasks/subtask/?id=" + id + "запроса от клиента.");

                manager.deleteSubTaskById(id);
                System.out.println("Сабтаск удален");
            }
            byte[] resp = new byte[0];
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, resp.length);
            exchange.getResponseBody().write(resp);
        }
        }

    public static void main(String[] args) throws IOException {
        HttpTaskManager manager = new HttpTaskManager("http://localhost:8078");
        HttpTaskServer taskServer = new HttpTaskServer(manager);
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
