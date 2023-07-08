import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.KVServer;
import service.HttpTaskManager;
import utils.Managers;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest extends TaskManagerTest <HttpTaskManager> {

    protected KVServer server;


    @BeforeEach
    public void init() throws IOException {
        server = new KVServer();
        server.start();
        taskManager = (HttpTaskManager) Managers.getDefault();

    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void loadFromServerTest() throws IOException {

        int taskId = taskManager.addSimpleTask(new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW, 100, Instant.ofEpochSecond(1000)));

        int epicId = taskManager.addEpic(new Epic("сдать экзамен", "сессия в мае ", 0));

        int subtaskId = taskManager.addSubTask(new SubTask("купить учебник", " в буквоеде скидки ", 0, TaskStatus.NEW,
                100, Instant.ofEpochSecond(16000), 2));


        int subtask2Id = taskManager.addSubTask(new SubTask("купить учебник2", " в буквоеде скидки2 ", 0, TaskStatus.NEW,
                100, Instant.ofEpochSecond(1600000), 2));

        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubTaskById(subtaskId);
        taskManager.getSubTaskById(subtask2Id);

        taskManager.load();
        Map<Integer, SubTask> subtasks = taskManager.getSubTasks();
        List<Task> history = taskManager.getHistory();


        Assertions.assertAll("Проверка восстановления состояния: ",
                () ->assertNotNull(subtasks),
                () ->assertEquals(2, subtasks.size(),"размеры списков сабтасков не совпадают"),
                () -> assertNotNull(history),
                () -> assertEquals(4, history.size(),"размеры списков истории не совпадают"));
    }
}