package Tests;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest <T extends TaskManager> {
protected T taskManager;
    private  static SimpleTask task1;
    private  static SimpleTask task2;
    private  static Epic epic1;
    @BeforeAll
    static void beforeAll() {
        task1 = new SimpleTask("name1", "description1", 0, TaskStatus.NEW);
        task2 = new SimpleTask("name2", "description2", 0, TaskStatus.DONE);
        epic1 = new Epic("name1", "description1", 0);

    }
    @Test
    public  void testGetListOfSimpleTasks() throws IOException {
        SimpleTask task1 = new SimpleTask("name1", "description1", 0, TaskStatus.NEW);
        SimpleTask task2 = new SimpleTask("name2", "description2", 0, TaskStatus.DONE);
        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);
        List<SimpleTask> arr = new ArrayList<>();
        arr.add(task1);
        arr.add(task2);

        assertEquals(arr,taskManager.getListOfSimpleTasks());

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

        assertEquals(arr,taskManager.getListOfEpics());
    }

    @Test
    public void testGetListOfsubTasks() throws IOException {
        Epic epic = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic);
        SubTask sub1 = new SubTask("name1", "description1", 0,  TaskStatus.NEW, epic.getId());
        SubTask sub2 = new SubTask("name2", "description2", 0,  TaskStatus.NEW, epic.getId());
        taskManager.addSubTask(sub1);
        taskManager.addSubTask(sub2);
        List<SubTask> arr = new ArrayList<>();
        arr.add(sub1);
        arr.add(sub2);

        assertEquals(arr,taskManager.getListOfsubTasks());
    }

    @Test
    public void testDeleteAllSimpleTasks() throws IOException {
        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);
        taskManager.deleteAllSimpleTasks();

        assertEquals(new ArrayList<>(), taskManager.getListOfSimpleTasks());
    };

    @Test
    public void testDeleteAllEpics() throws IOException{
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.deleteAllEpics();

        Assertions.assertAll("Проверка удаления эпиков: ",
                () -> assertEquals(new ArrayList<>(), taskManager.getListOfEpics()),
                () -> assertEquals(new ArrayList<>(), taskManager.getListOfsubTasks()));
    };

    @Test
    public void testDeleteAllSubTasks() throws IOException{
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.deleteAllSubTasks();

        Assertions.assertAll("Проверка удаления сабтасков: ",
                () -> assertEquals(new ArrayList<>(), taskManager.getListOfsubTasks()),
                () -> assertEquals(new ArrayList<>(), epic3.getSubsId()));
    };

    @Test
    public void testGetTaskById() throws IOException {
        taskManager.addSimpleTask(task1);
        List<Task> expected = new ArrayList<>();
        expected.add(task1);

        Assertions.assertAll("Проверка полученяи задач по айди: ",
                () -> assertEquals(taskManager.getTaskById(task1.getId()),task1, "Задачи с таким id не существует"),
                () -> assertEquals(expected, taskManager.getHistory()));
    };

    @Test
    public void testGetEpicById() throws IOException{
        taskManager.addEpic(epic1);
        List<Task> expected = new ArrayList<>();
        expected.add(epic1);

        Assertions.assertAll("Проверка полученяи эпиков по айди: ",
                () -> assertEquals(taskManager.getEpicById(epic1.getId()),epic1,"Эпика с таким id не существует"),
                () -> assertEquals(expected, taskManager.getHistory()));
    };

    @Test
    public void testGetSubTaskById() throws IOException{
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,epic3.getId());
        taskManager.addSubTask(sub3);
        List<Task> expected = new ArrayList<>();
        expected.add(sub3);

        Assertions.assertAll("Проверка полученяи сабтасков по айди: ",
                () -> assertEquals(taskManager.getSubTaskById(sub3.getId()),sub3, "Сабтаска с таким id не существует"),
                () -> assertEquals(expected, taskManager.getHistory()));
    };



    @Test
    public void testUpdateSimpleTask() throws IOException {
        taskManager.addSimpleTask(task1);
        SimpleTask task5 = new SimpleTask("name2", "description2", task1.getId(), TaskStatus.DONE);
        taskManager.updateSimpleTask(task5);

    Assertions.assertEquals(task5, taskManager.getTaskById(task5.getId()));
    };

    @Test
    public void testUpdateEpic() throws IOException {
        taskManager.addEpic(epic1);
        Epic epic3 = new Epic("name1", "description1", epic1.getId());
        taskManager.updateEpic(epic3);

        Assertions.assertEquals(epic3, taskManager.getEpicById(epic3.getId()));
    };

    @Test
    public void testUpdateSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,epic3.getId());
        taskManager.addSubTask(sub3);
        SubTask sub4 = new SubTask("name11", "description11", sub3.getId(),  TaskStatus.NEW,epic3.getId());
        taskManager.updateSubTask(sub4);

        Assertions.assertEquals(sub4, taskManager.getSubTaskById(sub4.getId()));
    };

    @Test
    public void testDeleteSimpleTaskById() throws IOException {
        taskManager.addSimpleTask(task1);
        taskManager.getTaskById(1);
        taskManager.deleteSimpleTaskById(1);
        List<Task> thisHistory = taskManager.getHistory();

        Assertions.assertAll("Проверка удаления задач по айди: ",
                () -> Assertions.assertNull(taskManager.getTaskById(task1.getId())),
                () -> Assertions.assertEquals(new ArrayList<>(),thisHistory));
    };

    @Test
    public void testDeleteEpicById() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.getEpicById(epic3.getId());
        taskManager.deleteEpicById(epic3.getId());
        List<Task> thisHistory = taskManager.getHistory();


        Assertions.assertAll("Проверка полученяи задач по айди: ",
                () -> Assertions.assertNull(taskManager.getEpicById(epic3.getId())),
                () -> Assertions.assertEquals(new ArrayList<>(),thisHistory),
                () -> Assertions.assertEquals(new ArrayList<>(),taskManager.getListOfSubsOfEpic(epic3)),
                () -> Assertions.assertEquals(new ArrayList<>(), taskManager.getListOfEpics()));
    };

    @Test
    public void testDeleteSubTaskById() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0,  TaskStatus.IN_PROGRESS,epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.getSubTaskById(sub3.getId());
        taskManager.deleteSubTaskById(sub3.getId());
        List<Task> thisHistory = taskManager.getHistory();


        Assertions.assertAll("Проверка полученяи задач по айди: ",
                () -> Assertions.assertNull(taskManager.getSubTaskById(sub3.getId())),
                () -> Assertions.assertEquals(new ArrayList<>(),thisHistory),
                () -> Assertions.assertEquals(new ArrayList<>(), taskManager.getListOfSubsOfEpic(epic3)),
                () -> Assertions.assertEquals(TaskStatus.NEW,taskManager.getEpicById(epic3.getId()).getStatus()),
                () -> Assertions.assertEquals(new ArrayList<>(), taskManager.getListOfsubTasks()));
    };

    @Test
    public void testGetListOfSubsOfEpic() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub1 = new SubTask("name1", "description1", 0,  TaskStatus.NEW, epic3.getId());
        SubTask sub2 = new SubTask("name2", "description2", 0,  TaskStatus.NEW, epic3.getId());
        taskManager.addSubTask(sub1);
        taskManager.addSubTask(sub2);

        List<SubTask> subId = new ArrayList<>();
        subId.add(sub1);
        subId.add(sub2);

        assertEquals(subId,taskManager.getListOfSubsOfEpic(epic3));
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

        Assertions.assertEquals(thisHistory, taskManager.getHistory());
    };
}