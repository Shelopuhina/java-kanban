package Tests;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.TaskType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }
    @Test
    public void  testAddTask(){
        Task task1 = new Task("name1", "description1", 1, TaskStatus.NEW, TaskType.SIMPLE_TASK,100, Instant.ofEpochSecond(1000));
        Epic task2 = new Epic("name2", "description2", 2);
        SubTask task3 = new SubTask("name3", "description3", 3, TaskStatus.NEW, 100, Instant.ofEpochSecond(1400),task2.getId());


        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.addTask(task3);//проверка дублирования

        List <Task> history = new ArrayList<>();
        history.add(task1);
        history.add(task2);
        history.add(task3);


        assertEquals(history, historyManager.getHistory());


    }
    @Test
    public void removeBegin(){
        Task task1 = new Task("name1", "description1", 1, TaskStatus.NEW, TaskType.SIMPLE_TASK,100,Instant.ofEpochSecond(1000));
        Epic task2 = new Epic("name2", "description2", 2);
        SubTask task3 = new SubTask("name3", "description3", 3, TaskStatus.NEW, 100, Instant.ofEpochSecond(1400),task2.getId());
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        List <Task> history = new ArrayList<>();
        history.add(task2);
        history.add(task3);
        historyManager.remove(task1.getId());//проверка удаления из начала

        assertEquals(history, historyManager.getHistory());

    }
    @Test
    public void removeMiddle(){
        Task task1 = new Task("name1", "description1", 1, TaskStatus.NEW, TaskType.SIMPLE_TASK,100,Instant.ofEpochSecond(1000));
        Epic task2 = new Epic("name2", "description2", 2);
        SubTask task3 = new SubTask("name3", "description3", 3, TaskStatus.NEW, 100, Instant.ofEpochSecond(1400),task2.getId());
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        List <Task> history = new ArrayList<>();
        history.add(task1);
        history.add(task3);
        historyManager.remove(task2.getId());//проверка удаления из середины

        assertEquals(history, historyManager.getHistory());

    }
    @Test
    public void removeУтв(){
        Task task1 = new Task("name1", "description1", 1, TaskStatus.NEW, TaskType.SIMPLE_TASK,100,Instant.ofEpochSecond(1000));
        Epic task2 = new Epic("name2", "description2", 2);
        SubTask task3 = new SubTask("name3", "description3", 3, TaskStatus.NEW, 100, Instant.ofEpochSecond(1400),task2.getId());
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        List <Task> history = new ArrayList<>();
        history.add(task1);
        history.add(task2);
        historyManager.remove(task3.getId());//проверка удаления из конца

        assertEquals(history, historyManager.getHistory());

    }
    @Test
    public void testGetHistory(){
        List <Task> history = new ArrayList<>();
        assertEquals(history, historyManager.getHistory());//проверка пустого списка истории
    }
}
