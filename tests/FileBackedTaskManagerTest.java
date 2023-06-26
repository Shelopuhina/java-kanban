import model.Epic;
import model.SimpleTask;
import model.TaskStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import service.*;
import java.io.*;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class FileBackedTaskManagerTest extends TaskManagerTest <FileBackedTasksManager> {

    private static File testFile;
private FileBackedTasksManager fileManager;
    @BeforeEach
     void beforeEach() throws IOException {
       testFile = new File("C:\\Users\\T-3000\\dev\\java-kanban\\src\\service", "fileForTest.txt");
        taskManager = new FileBackedTasksManager(testFile);
    }
    @AfterEach
    void afterEach() {
        try {
            testFile.delete();
        } catch (Exception ex) {
            ex.getMessage();
        }
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
    public void LoadFromEmptyFile() throws IOException {
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
