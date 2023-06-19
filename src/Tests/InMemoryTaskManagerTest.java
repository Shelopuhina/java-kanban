package Tests;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {



    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testAddSimpleTask() throws IOException {
        SimpleTask task1 = new SimpleTask("name1", "description1", 0, TaskStatus.NEW);
        int result = taskManager.addSimpleTask(task1);
        Map<Integer, SimpleTask> simpleTask = new HashMap<>();
        simpleTask.put(task1.getId(), task1);

        Assertions.assertAll("Проверка добавления задачи: ",
                () -> assertEquals(1, result),//проверка возврата значения Id
                () -> assertEquals(simpleTask, taskManager.getSimpleTask()),//проверка мапы
                () -> assertEquals(2, taskManager.getNextId()));//проверка увеличения nextId
    }

    @Test
    public void testAddEpic() throws IOException {
        Epic epic1 = new Epic("name1", "description1", 0);
        int result = taskManager.addEpic(epic1);
        Map<Integer, Epic> epicMap = new HashMap<>();
        epicMap.put(epic1.getId(), epic1);

        Assertions.assertAll("Проверка добавления эпика: ",
                () -> assertEquals(1, result),
                () -> assertEquals(epicMap, taskManager.getEpics()),
                () -> assertEquals(2, taskManager.getNextId()));
    }

    @Test
    public void testAddSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.IN_PROGRESS, epic3.getId());
        int result = taskManager.addSubTask(sub3);
        List<Integer> expected = new ArrayList<>();
        expected.add(sub3.getId());
        Map<Integer, SubTask> subTaskMap = new HashMap<>();
        subTaskMap.put(sub3.getId(), sub3);

        Assertions.assertAll("Проверка добавления сабтаска: ", //() -> assertEquals("Вашингтон", address.getCity()),
                () -> Assertions.assertEquals(2, result),
                () -> Assertions.assertEquals(subTaskMap, taskManager.getSubTasks()),
                () -> Assertions.assertEquals(3, taskManager.getNextId()),
                () -> Assertions.assertEquals(expected, epic3.getSubsId()),//проверка списка айдишников в поле эпика
                () -> Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic3.getStatus()));//проверка обновления статуса
    }

    @Test
    public void testUpdateSimpleTask() {
    }

    ;

    @Test
    public void testUpdateEpicStatusNullSubTask() throws IOException {
        Epic epic1 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic1);
        Assertions.assertEquals(TaskStatus.NEW, taskManager.updateEpicStatus(epic1));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }

    @Test
    public void testUpdateEpicStatusNewSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.NEW, epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.NEW, epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        Assertions.assertEquals(TaskStatus.NEW, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать);
    }

    @Test
    public void testUpdateEpicStatusDoneSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.DONE, epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.DONE, epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        Assertions.assertEquals(TaskStatus.DONE, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }

    @Test
    public void testUpdateEpicStatusInProgressSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.IN_PROGRESS, epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.NEW, epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }

    @Test
    public void testUpdateEpicStatusNewDoneSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.NEW, epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.DONE, epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }
}

