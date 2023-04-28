package service;

import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, EpicStatus.NEW);
        manager.addSimpleTask(task1);
        SimpleTask task2 = new SimpleTask("Приготовить ужин", "Плов", 0, EpicStatus.DONE);
        manager.addSimpleTask(task2);
        System.out.println(task1.getId()+task1.getName()+task1.getDescription()+task1.getStatus());
        System.out.println(task2.getId()+task2.getName()+task2.getDescription()+task2.getStatus());

        Epic epic1 = new Epic("сдать экзамен", "сессия в мае", 0, EpicStatus.NEW);
        manager.addEpic(epic1);

        SubTask subTask1 = new SubTask("купить учебник", "в буквоеде скидки", 0,EpicStatus.NEW,
                epic1.getId());
        manager.addSubTask(subTask1);
        //System.out.println(subTask1.getId()+subTask1.getName()+subTask1.getDescription()+subTask1.getStatus());

        SubTask subTask2 = new SubTask("скачать вопросник", "ссылка на сайте уника", 0,EpicStatus.DONE,
                epic1.getId());
        manager.addSubTask(subTask2);
        //System.out.println(subTask2.getId()+subTask2.getName()+subTask2.getDescription()+subTask2.getStatus());
        System.out.println(epic1.getStatus());

        Epic epic2 = new Epic("получить диплом", "через 6 месяцев", 0, EpicStatus.NEW);
        manager.addEpic(epic2);
        //System.out.println(epic2.getId()+epic2.getName()+epic2.getDescription()+epic2.getStatus());
        System.out.println(epic2.getStatus());

        SubTask subTask3 = new SubTask("купить прописи", "в буквоеде скидки", 0,EpicStatus.DONE,
                epic2.getId());
        manager.addSubTask(subTask3);
        //System.out.println(subTask3.getId()+subTask3.getName()+subTask3.getDescription()+subTask3.getStatus()
        // +subTask3.getEpicId());

        /*System.out.println(manager.simpleTask);
        System.out.println(manager.epics);
        System.out.println(manager.subTasks);*/

        System.out.println( manager.getListOfSimpleTasks());
        System.out.println( manager.getListOfEpics());
        System.out.println( manager.getListOfsubTasks());
        //System.out.println( manager.simpleTask);

        //manager.getListOfSubsOfEpic(epic1);

        /*System.out.println(epic1.getSubsId());
        manager.deleteSubTaskById(subTask1.getId(),epic1);
        System.out.println(epic1.getSubsId());
        System.out.println(manager.subTasks);*/

        SimpleTask task3 = new SimpleTask("Собрать коробки", "Для переезда", task1.getId(), EpicStatus.DONE);
        manager.updateSimpleTask(task3);
        System.out.println(task3.getId()+task3.getName()+task3.getDescription()+task3.getStatus());


        SimpleTask task4 = new SimpleTask("Приготовить ужин", "Плов", task2.getId(), EpicStatus.NEW);
        manager.updateSimpleTask(task4);
        System.out.println(task4.getId()+task4.getName()+task4.getDescription()+task4.getStatus());


        SubTask subTask5 = new SubTask("купить учебник", "в буквоеде скидки", subTask1.getId(), EpicStatus.NEW,
                epic1.getId());
        manager.updateSubTask(subTask5);
        System.out.println(subTask5.getId()+subTask5.getName()+subTask5.getDescription()+subTask5.getStatus()
                +subTask5.getEpicId());

        SubTask subTask6 = new SubTask("скачать вопросник", "ссылка на сайте уника", subTask2.getId(),EpicStatus.DONE,
                epic1.getId());
        manager.updateSubTask(subTask6);
        System.out.println(subTask6.getId()+subTask6.getName()+subTask6.getDescription()+subTask6.getStatus()
                +subTask6.getEpicId());

        SubTask subTask4 = new SubTask("заниматься по часу в день", "с 15 до 16", subTask3.getId(),EpicStatus.NEW,
                epic2.getId());
        manager.updateSubTask(subTask4);
        System.out.println(subTask4.getId()+subTask4.getName()+subTask4.getDescription()+subTask4.getStatus()
                +subTask4.getEpicId());
        System.out.println(epic1.getStatus());
        System.out.println(epic2.getStatus());

        Epic epic5 = new Epic("сдать экзамен", "сессия в июне", epic1.getId(), EpicStatus.NEW);
        manager.updateEpic(epic5);

        Epic epic4 = new Epic("получить red диплом", "через 6 месяцев", epic2.getId(), EpicStatus.NEW);
        manager.updateEpic(epic4);

        manager.getEpicById(1);
        manager.getEpicById(2);
        manager.getSubTaskById(1);
        manager.getSubTaskById(2);
        manager.getSubTaskById(3);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(1);
        manager.getEpicById(2);
        manager.getSubTaskById(1);
        manager.getEpicById(1);
        manager.getTaskById(1);
        manager.getTaskById(2);


        System.out.println(manager.getHistory());

        System.out.println(manager.getListOfSimpleTasks());
        System.out.println(manager.getListOfEpics());
        System.out.println(manager.getListOfsubTasks());

        System.out.println(manager.getListOfSubsOfEpic(epic1));
        manager.deleteAllSubTasks();

        System.out.println(manager.getListOfSubsOfEpic(epic1));
        System.out.println(epic5.getStatus());
        System.out.println(epic4.getStatus());

        /*System.out.println(manager.epics);
        System.out.println(manager.getSubTaskById(subTask4.getId()).getName());*/

        /*System.out.println(manager.epics);
        System.out.println(epic4.getSubsId());
        System.out.println(manager.subTasks);*/














    }
}
