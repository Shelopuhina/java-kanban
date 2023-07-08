import model.Epic;
import model.SimpleTask;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import service.FileBackedTasksManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest extends TaskManagerTest <FileBackedTasksManager> {
    @TempDir
    Path tempDir;
    File testFile;
    private FileBackedTasksManager fileManager;
    @BeforeEach
     void beforeEach() throws IOException {
        try {
            testFile = tempDir.resolve("fileForTest.txt").toFile();
        }
        catch(InvalidPathException ipe) {
            System.err.println(
                    "error creating temporary test file in " +
                            this.getClass().getSimpleName() );
        }
        taskManager = new FileBackedTasksManager();
    }

    @Test
    public void testSave() throws IOException {
        SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW,100,Instant.ofEpochSecond(1000));
        taskManager.addSimpleTask(task1);
        Epic epic1 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic1);

         String title = "id,type,name,status,description,duration,startTime,endTime,epic";
         String task = String.join(",", "1", task1.getType().toString(), task1.getName(),
                 task1.getStatus().toString(), task1.getDescription(),String.valueOf(task1.getDuration()),
                 String.valueOf(task1.getStartTime()), String.valueOf(task1.getEndTime()) );
         String emptyHistory = "История просмотров пуста";
        String epic = String.join(",", "2", epic1.getType().toString(), epic1.getName(),
                epic1.getStatus().toString(), epic1.getDescription(),String.valueOf(epic1.getDuration()),
                String.valueOf(epic1.getStartTime()),String.valueOf(epic1.getEndTime()) );

         String empty = "";
        List<String> strings = new ArrayList<>();
        Collections.addAll(strings,title, task,epic, empty, emptyHistory);

        FileReader reader = new FileReader(testFile.getName());
        BufferedReader br = new BufferedReader(reader);
        List<String> allStrings = new ArrayList<>();
        while (br.ready()) {
            String line = br.readLine();
            allStrings.add(line);
        }
        br.close();

        Assertions.assertEquals(strings.toString(),allStrings.toString());
    }

    @Test
    public void saveEmpty() throws IOException {
        SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW, 100,Instant.ofEpochSecond(1000));
        taskManager.addSimpleTask(task1);
        Epic epic1 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic1);
        taskManager.deleteAllEpics();
        taskManager.deleteAllSimpleTasks();

        FileReader reader = new FileReader(testFile.getName());
        BufferedReader br = new BufferedReader(reader);
        List<String> allStrings = new ArrayList<>();
        while (br.ready()) {
            String line = br.readLine();
            allStrings.add(line);
        }
        br.close();
        String title = "id,type,name,status,description,duration,startTime,endTime,epic";
        String emptyHistory = "История просмотров пуста";
        String empty = "";
        List<String> strings = new ArrayList<>();
        Collections.addAll(strings,title, empty, emptyHistory);

       assertEquals(strings.toString(),allStrings.toString());
    }

    @Test
    public void testLoadFromFile() throws IOException {
        SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW,100,Instant.ofEpochSecond(1000));
        taskManager.addSimpleTask(task1);
        Epic epic1 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic1);

        FileBackedTasksManager newFileManager = taskManager.loadFromFile(testFile);//пришлось сделать метод loadFromFile  в классе  FileBackedTasksManager - public иначе не могла его вызвать в тестовом методе
        Assertions.assertAll("Проверка сохранения состояния: ",
                () -> assertEquals(List.of(epic1).size(),newFileManager.getListOfEpics().size()),
                () -> assertEquals(List.of(task1).size(), newFileManager.getListOfSimpleTasks().size()),
                () -> assertEquals(new ArrayList<>(), newFileManager.getHistory()));

    }
    @Test
    public void loadFromFileTest() throws IOException {
        SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW,100,Instant.ofEpochSecond(1000));
        taskManager.addSimpleTask(task1);
        Epic epic1 = new Epic("name1", "description1", 0);
        taskManager.addEpic(epic1);
        taskManager.deleteAllEpics();
        taskManager.deleteAllSimpleTasks();

        FileBackedTasksManager newFileManager = taskManager.loadFromFile(testFile);

        assertEquals(new ArrayList<>(), newFileManager.getHistory());
    }
}
