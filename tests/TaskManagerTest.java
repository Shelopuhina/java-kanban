import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest <T extends TaskManager> {
protected T taskManager;
    private  static SimpleTask task1;
    private  static SimpleTask task2;
    private  static Epic epic1;
    @BeforeAll
    static void beforeAll() {
        task1 = new SimpleTask("name1", "description1", 0, TaskStatus.NEW,100, Instant.ofEpochSecond(1000));
        task2 = new SimpleTask("name2", "description2", 0, TaskStatus.DONE,100, Instant.ofEpochSecond(8000));
        epic1 = new Epic("name1", "description1", 0);

    }
    @Test
    public  void testGetListOfSimpleTasks() throws IOException {
        SimpleTask task1 = new SimpleTask("name1", "description1", 0, TaskStatus.NEW,100,Instant.ofEpochSecond(1000));
        SimpleTask task2 = new SimpleTask("name2", "description2", 0, TaskStatus.DONE,100, Instant.ofEpochSecond(8000));
        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);
        List<SimpleTask> arr = new ArrayList<>();
        arr.add(task1);
        arr.add(task2);

        Assertions.assertAll("Проверка получения списка задач: ",
                () -> assertEquals(arr,taskManager.getListOfSimpleTasks(), "Задачи не возвращаются."),
                () -> assertEquals(2, taskManager.getListOfSimpleTasks().size(), "Неверное количество задач."));

    }
    @Test
    public void testGetListOfEpics() throws IOException {
        Epic task1 = new Epic("name1", "description1", 0);
        Epic task2 = new Epic("name2", "description2", 0);
        taskManager.addEpic(task1);
        taskManager.addEpic(task2);
        List<Epic> arr = new ArrayList<>();
        arr.add(task1);
        arr.add(task2);

        Assertions.assertAll("Проверка получения списка эпиков: ",
                () -> assertEquals(arr,taskManager.getListOfEpics(), "Эпики не возвращаются."),
                () -> assertEquals(2, taskManager.getListOfEpics().size(),"Неверное количество эпиков."));
    }

    @Test
    public void testGetListOfsubTasks() throws IOException {
        Epic epic = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic);
        SubTask sub1 = new SubTask("name1", "description1", 0,  TaskStatus.NEW,100, Instant.ofEpochSecond(24000), epic.getId());
        SubTask sub2 = new SubTask("name2", "description2", 0,  TaskStatus.NEW,100, Instant.ofEpochSecond(32000), epic.getId());
        taskManager.addSubTask(sub1);
        taskManager.addSubTask(sub2);
        List<SubTask> arr = new ArrayList<>();
        arr.add(sub1);
        arr.add(sub2);

        Assertions.assertAll("Проверка получения списка сабтасков: ",
                () -> assertEquals(arr,taskManager.getListOfsubTasks(), "Сабтаски не возвращаются."),
                () -> assertEquals(2, taskManager.getListOfsubTasks().size(),"Неверное количество сабтасков."));
    }

    @Test
    public void testDeleteAllSimpleTasks() throws IOException {
        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);
        taskManager.deleteAllSimpleTasks();

         assertEquals(new ArrayList<>(), taskManager.getListOfSimpleTasks(), "Произошел сбой удаления задач.");
    };

    @Test
    public void testDeleteAllEpics() throws IOException{
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,100, Instant.ofEpochSecond(40000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.deleteAllEpics();

        Assertions.assertAll("Проверка удаления эпиков: ",
                () -> assertEquals(new ArrayList<>(), taskManager.getListOfEpics(), "Произошел сбой удаления эпиков."),
                () -> assertEquals(new ArrayList<>(), taskManager.getListOfsubTasks(), "Произошел сбой удаления сабтасков."));
    };

    @Test
    public void testDeleteAllSubTasks() throws IOException{
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,100, Instant.ofEpochSecond(40000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.deleteAllSubTasks();

        Assertions.assertAll("Проверка удаления сабтасков: ",
                () -> assertEquals(new ArrayList<>(), taskManager.getListOfsubTasks(), "Произошел сбой удаления сабтасков."),
                () -> assertEquals(new ArrayList<>(), epic3.getSubsId(), "Сабтаски не удалены из поля эпика."));
    };

    @Test
    public void testGetTaskById() throws IOException {
        taskManager.addSimpleTask(task1);
        List<Task> expected = new ArrayList<>();
        expected.add(task1);

        Assertions.assertAll("Проверка получения задач по айди: ",
                () -> assertNotNull(taskManager.getTaskById(task1.getId()), "Задачи с таким id не существует."),
                () -> assertEquals(taskManager.getTaskById(task1.getId()), task1, "Задачи не совпадают."),
                () -> assertEquals(expected, taskManager.getHistory(), "Ошибка добавления задачи в историю"));
    };

    @Test
    public void testGetEpicById() throws IOException{
        taskManager.addEpic(epic1);
        List<Task> expected = new ArrayList<>();
        expected.add(epic1);

        Assertions.assertAll("Проверка полученяи эпиков по айди: ",
                () -> assertNotNull(taskManager.getEpicById(epic1.getId()),"Эпика с таким id не существует."),
                () -> assertEquals(taskManager.getEpicById(epic1.getId()), epic1, "Эпики не совпадают."),
                () -> assertEquals(expected, taskManager.getHistory(),"Ошибка добавления эпика в историю."));
    };

    @Test
    public void testGetSubTaskById() throws IOException{
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,100, Instant.ofEpochSecond(40000),epic3.getId());
        taskManager.addSubTask(sub3);
        List<Task> expected = new ArrayList<>();
        expected.add(sub3);

        Assertions.assertAll("Проверка получения сабтасков по айди: ",
                () -> assertNotNull(taskManager.getSubTaskById(sub3.getId()),"Эпика с таким id не существует."),
                () -> assertEquals(taskManager.getSubTaskById(sub3.getId()),sub3, "Сабтаски не совпадают."),
                () -> assertEquals(expected, taskManager.getHistory(), "Ошибка добавления сабтаска в историю."));
    };



    @Test
    public void testUpdateSimpleTask() throws IOException {
        taskManager.addSimpleTask(task1);
        SimpleTask task5 = new SimpleTask("name2", "description2", task1.getId(), TaskStatus.DONE,100,Instant.ofEpochSecond(48000));
        taskManager.updateSimpleTask(task5);

    Assertions.assertEquals(task5, taskManager.getTaskById(task5.getId()), "Задачи не совпадают.");
    };

    @Test
    public void testUpdateEpic() throws IOException {
        taskManager.addEpic(epic1);
        Epic epic3 = new Epic("name1", "description1", epic1.getId());
        taskManager.updateEpic(epic3);

        Assertions.assertEquals(epic3, taskManager.getEpicById(epic3.getId()), "Эпики не совпадают.");
    };

    @Test
    public void testUpdateSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,100, Instant.ofEpochSecond(40000),epic3.getId());
        taskManager.addSubTask(sub3);
        SubTask sub4 = new SubTask("name11", "description11", sub3.getId(),  TaskStatus.NEW,100, Instant.ofEpochSecond(56000),epic3.getId());
        taskManager.updateSubTask(sub4);

        Assertions.assertEquals(sub4, taskManager.getSubTaskById(sub4.getId()), "Сабтаски не совпадают.");
    };

    @Test
    public void testDeleteSimpleTaskById() throws IOException {
        taskManager.addSimpleTask(task1);
        taskManager.getTaskById(1);
        taskManager.deleteSimpleTaskById(1);
        List<Task> thisHistory = taskManager.getHistory();

        Assertions.assertAll("Проверка удаления задачи по айди: ",
                () -> Assertions.assertNull(taskManager.getTaskById(task1.getId()), "Задача не удалена."),
                () -> Assertions.assertEquals(new ArrayList<>(),thisHistory, "Задача не удалена из истории."));
    };

    @Test
    public void testDeleteEpicById() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,100, Instant.ofEpochSecond(40000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.getEpicById(epic3.getId());
        taskManager.deleteEpicById(epic3.getId());
        List<Task> thisHistory = taskManager.getHistory();


        Assertions.assertAll("Проверка удаления эпика по айди: ",
                () -> Assertions.assertNull(taskManager.getEpicById(epic3.getId()), "Эпик не удален."),
                () -> Assertions.assertEquals(new ArrayList<>(),thisHistory, "Эпик не удален из истории."),
                () -> Assertions.assertEquals(new ArrayList<>(),taskManager.getListOfSubsOfEpic(epic3), "Сабтаски эпика не очищены."),
                () -> Assertions.assertEquals(new ArrayList<>(), taskManager.getListOfEpics(), "Эпик не удален из мапы."));
    };

    @Test
    public void testDeleteSubTaskById() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,100, Instant.ofEpochSecond(40000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.getSubTaskById(sub3.getId());
        taskManager.deleteSubTaskById(sub3.getId());
        List<Task> thisHistory = taskManager.getHistory();


        Assertions.assertAll("Проверка удаления сабтаска по айди: ",
                () -> Assertions.assertNull(taskManager.getSubTaskById(sub3.getId()),"Сабтаск не удален."),
                () -> Assertions.assertEquals(new ArrayList<>(),thisHistory, "Сабтаск не удален из истории."),
                () -> Assertions.assertEquals(new ArrayList<>(), taskManager.getListOfSubsOfEpic(epic3), "Сабтаск не удален из поля эпика."),
                () -> Assertions.assertEquals(TaskStatus.NEW,taskManager.getEpicById(epic3.getId()).getStatus(), "Статус эпика после удаления сабтаска не обновился."),
                () -> Assertions.assertEquals(new ArrayList<>(), taskManager.getListOfsubTasks(), "Сабтаск не удален из мапы"));
    };

    @Test
    public void testGetListOfSubsOfEpic() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub1 = new SubTask("name1", "description1", 0,  TaskStatus.NEW,100, Instant.ofEpochSecond(16000), epic3.getId());
        SubTask sub2 = new SubTask("name2", "description2", 0,  TaskStatus.NEW,100, Instant.ofEpochSecond(24000), epic3.getId());
        taskManager.addSubTask(sub1);
        taskManager.addSubTask(sub2);

        List<SubTask> subId = new ArrayList<>();
        subId.add(sub1);
        subId.add(sub2);

        assertEquals(subId,taskManager.getListOfSubsOfEpic(epic3), "Сабтаск не удален из поля эпика.");
    }

    @Test
    public void testGetHistory() throws IOException {
       taskManager.addEpic(epic1);
       taskManager.addSimpleTask(task1);
       taskManager.getEpicById(epic1.getId());
       taskManager.getTaskById(task1.getId());
       List<Task> thisHistory = new ArrayList<>();
       thisHistory.add(epic1);
       thisHistory.add(task1);

        Assertions.assertAll("Проверка удаления сабтаска по айди: ",
                () -> assertNotNull(taskManager.getHistory(), "История не пустая."),
                () -> assertEquals(thisHistory, taskManager.getHistory()),
                () -> assertEquals(2, taskManager.getHistory().size(), "История не пустая."));
    };
}