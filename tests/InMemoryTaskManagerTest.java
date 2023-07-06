import model.*;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {



    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testAddSimpleTask() throws IOException {
        SimpleTask task1 = new SimpleTask("name1", "description1", 0, TaskStatus.NEW,100, Instant.ofEpochSecond(1000));
        int result = taskManager.addSimpleTask(task1);
        Map<Integer, SimpleTask> simpleTask = new HashMap<>();
        simpleTask.put(task1.getId(), task1);

        Assertions.assertAll("Проверка добавления задачи: ",
                () -> assertNotNull(taskManager.getTaskById(1), "Задача не найдена."),
                () -> assertEquals(1, result),//проверка возврата значения Id
                () -> assertEquals(simpleTask, taskManager.getSimpleTask(), "Задача не записана в мапу."),//проверка мапы
                () -> assertEquals(2, taskManager.getNextId(), "Некорректное значение счетчика id."));//проверка увеличения nextId
    }

    @Test
    public void testAddEpic() throws IOException {
        Epic epic1 = new Epic("name1", "description1", 0);
        int result = taskManager.addEpic(epic1);
        Map<Integer, Epic> epicMap = new HashMap<>();
        epicMap.put(epic1.getId(), epic1);

        Assertions.assertAll("Проверка добавления эпика: ",
                () -> assertNotNull(taskManager.getEpicById(1), "Эпик не найден."),
                () -> assertEquals(1, result),
                () -> assertEquals(epicMap, taskManager.getEpics(), "Эпик не записан в мапу."),
                () -> assertEquals(2, taskManager.getNextId(), "Некорректное значение счетчика id."));
    }

    @Test
    public void testAddSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.IN_PROGRESS, 100, Instant.ofEpochSecond(8000),epic3.getId());
        int result = taskManager.addSubTask(sub3);
        List<Integer> expected = new ArrayList<>();
        expected.add(sub3.getId());
        Map<Integer, SubTask> subTaskMap = new HashMap<>();
        subTaskMap.put(sub3.getId(), sub3);

        Assertions.assertAll("Проверка добавления сабтаска: ", //() -> assertEquals("Вашингтон", address.getCity()),
                () -> assertNotNull(taskManager.getSubTaskById(2), "Сабтаск не найден."),
                () -> Assertions.assertEquals(2, result),
                () -> assertEquals(subTaskMap, taskManager.getSubTasks(), "Сабтаск не записан в мапу."),
                () -> assertEquals(3, taskManager.getNextId(), "Некорректное значение счетчика id."),
                () -> Assertions.assertEquals(expected, epic3.getSubsId(), "Сабтаск не записан в поле эпика"),//проверка списка айдишников в поле эпика
                () -> Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic3.getStatus(), "Статус эпика после добавления сабтаска не обновился"));//проверка обновления статуса
    }

        @Test
    public void testUpdateEpicStatusNullSubTask() throws IOException {
        Epic epic1 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic1);
        assertEquals(TaskStatus.NEW, taskManager.updateEpicStatus(epic1));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }

    @Test
    public void testUpdateEpicStatusNewSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.NEW, 100, Instant.ofEpochSecond(8000),epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.NEW, 100, Instant.ofEpochSecond(16000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        assertEquals(TaskStatus.NEW, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать);
    }

    @Test
    public void testUpdateEpicStatusDoneSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.DONE,100, Instant.ofEpochSecond(8000),epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.DONE,100, Instant.ofEpochSecond(16000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        assertEquals(TaskStatus.DONE, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }

    @Test
    public void testUpdateEpicStatusInProgressSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.IN_PROGRESS,100, Instant.ofEpochSecond(8000),epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.NEW,100, Instant.ofEpochSecond(16000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }

    @Test
    public void testUpdateEpicStatusNewDoneSubTask() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.NEW, 100, Instant.ofEpochSecond(8000),epic3.getId());
        SubTask sub4 = new SubTask("name1", "description1", 0, TaskStatus.DONE, 100, Instant.ofEpochSecond(16000),epic3.getId());
        taskManager.addSubTask(sub3);
        taskManager.addSubTask(sub4);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.updateEpicStatus(epic3));//пришлось сделать метод updateEpicStatus() публичным, иначе хз, как его вызвать
    }
    @Test
    public void testGetEpicStartEndTime() throws IOException {
        Epic epic3 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic3);
        SubTask sub3 = new SubTask("name1", "description1", 0, TaskStatus.NEW, 100, Instant.ofEpochSecond(8000),epic3.getId());
        taskManager.addSubTask(sub3);
        Assertions.assertAll("Проверка добавления сабтаска: ",
                () -> assertEquals(Instant.ofEpochSecond(8000), epic3.getStartTime()),
                () -> assertEquals(100, epic3.getDuration()),
                () -> assertEquals(sub3.getEndTime(),epic3.getEndTime()));
    }
}

