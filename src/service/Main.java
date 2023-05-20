package service;

import model.Epic;
import model.SimpleTask;
import model.SubTask;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, EpicStatus.NEW);
        manager.addSimpleTask(task1);
        SimpleTask task2 = new SimpleTask("Приготовить ужин", "Плов", 0, EpicStatus.DONE);
        manager.addSimpleTask(task2);
        System.out.println(task1.getId()+task1.getName()+task1.getDescription()+task1.getStatus());
        System.out.println(task2.getId()+task2.getName()+task2.getDescription()+task2.getStatus());

        Epic epic1 = new Epic("сдать экзамен", "сессия в мае ", 0, EpicStatus.NEW);
        manager.addEpic(epic1);
        System.out.println(epic1.getId()+epic1.getName()+epic1.getDescription()+epic1.getStatus());

        SubTask subTask1 = new SubTask("купить учебник", " в буквоеде скидки ", 0,EpicStatus.NEW,
                epic1.getId());
        manager.addSubTask(subTask1);
        System.out.println(subTask1.getId()+subTask1.getName()+subTask1.getDescription()+subTask1.getStatus()+ subTask1.getEpicId());

        SubTask subTask2 = new SubTask("скачать вопросник", "ссылка на сайте уника ", 0,EpicStatus.DONE,
                epic1.getId());
        manager.addSubTask(subTask2);
        System.out.println(subTask2.getId()+subTask2.getName()+subTask2.getDescription()+subTask2.getStatus()+ subTask2.getEpicId());
        System.out.println(epic1.getStatus());

        SubTask subTask3 = new SubTask("купить прописи", "в буквоеде  ", 0,EpicStatus.DONE,
                epic1.getId());
        manager.addSubTask(subTask3);
        System.out.println(subTask3.getId()+subTask3.getName()+subTask3.getDescription()+subTask3.getStatus()
                +subTask3.getEpicId());
        System.out.println("Статус эпик 1 "+epic1.getStatus());

        Epic epic2 = new Epic("получить диплом", "через 6 месяцев ", 0, EpicStatus.NEW);
        manager.addEpic(epic2);
        System.out.println(epic2.getId()+epic2.getName()+epic2.getDescription()+epic2.getStatus());
        System.out.println(epic2.getStatus());

        manager.getEpicById(3);
        manager.getEpicById(7);
        manager.getSubTaskById(4);
        manager.getSubTaskById(4);
        manager.getSubTaskById(5);
        manager.getTaskById(2);

        manager.deleteSimpleTaskById(2);//удалила задачу, чтобы убедиться, что она не попадет в историю
        manager.deleteEpicById(3);//удалила эпик, чтобы убедиться, что его подзадачи стираются из истории

        System.out.println("История просмотров: "+manager.getHistory());
    }
}
