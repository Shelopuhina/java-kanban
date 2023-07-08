import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.enums.TaskStatus;
import servers.KVServer;
import service.HttpTaskManager;
import service.TaskManager;
import utils.Managers;

import java.io.IOException;
import java.time.Instant;

public class Main {
    public static void main(String[] args) throws IOException {

        new KVServer().start();
        TaskManager manager = Managers.getDefault();


       SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, TaskStatus.NEW,100, Instant.ofEpochSecond(1000));
        manager.addSimpleTask(task1);
        SimpleTask task2 = new SimpleTask("Приготовить ужин", "Плов", 0, TaskStatus.DONE,100, Instant.ofEpochSecond(8000));
        manager.addSimpleTask(task2);
       // System.out.println(task1.getId() + task1.getName() + task1.getDescription() + task1.getStatus());
        //System.out.println(task2.getId() + task2.getName() + task2.getDescription() + task2.getStatus());

        Epic epic1 = new Epic("сдать экзамен", "сессия в мае ", 0);
        manager.addEpic(epic1);

        //System.out.println(epic1.getId() + epic1.getName() + epic1.getDescription() + epic1.getStatus());

        SubTask subTask1 = new SubTask("купить учебник", " в буквоеде скидки ", 0, TaskStatus.NEW,
                100, Instant.ofEpochSecond(16000),epic1.getId());
        manager.addSubTask(subTask1);
        //System.out.println(subTask1.getId() + subTask1.getName() + subTask1.getDescription() + subTask1.getStatus() + subTask1.getEpicId());

        SubTask subTask2 = new SubTask("скачать вопросник", "ссылка на сайте уника ", 0, TaskStatus.DONE,
                100, Instant.ofEpochSecond(24000),epic1.getId());
        manager.addSubTask(subTask2);
       // System.out.println(subTask2.getId() + subTask2.getName() + subTask2.getDescription() + subTask2.getStatus() + subTask2.getEpicId());
       // System.out.println(epic1.getStatus());

        SubTask subTask3 = new SubTask("купить прописи", "в буквоеде  ", 0, TaskStatus.DONE,
                100, Instant.ofEpochSecond(32000),epic1.getId());
        manager.addSubTask(subTask3);


        Epic epic2 = new Epic("получить диплом", "через 6 месяцев ", 0);
        manager.addEpic(epic2);


        manager.getEpicById(3);
        manager.getEpicById(7);
        manager.getSubTaskById(4);
        manager.getSubTaskById(4);
        manager.getSubTaskById(5);
        manager.getTaskById(2);
     System.out.println("last: "+manager.getPrioritizedTasks());
        HttpTaskManager newManager = new HttpTaskManager("http://localhost:8078");


    }
}
