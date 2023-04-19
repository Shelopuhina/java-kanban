package service;

import model.Epic;
import model.SimpleTask;
import model.SubTask;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        SimpleTask task1 = new SimpleTask("Собрать коробки", "Для переезда", 0, "NEW");
        taskManager.addSimpleTask(task1);
        SimpleTask task2 = new SimpleTask("Приготовить ужин", "Плов", 0, "DONE");
        taskManager.addSimpleTask(task2);
        System.out.println(task1.getId()+task1.getName()+task1.getDescription()+task1.getStatus());
        System.out.println(task2.getId()+task2.getName()+task2.getDescription()+task2.getStatus());

        Epic epic1 = new Epic("сдать экзамен", "сессия в мае", 0, "NEW");
        taskManager.addEpic(epic1);


        SubTask subTask1 = new SubTask("купить учебник", "в буквоеде скидки", 0,"NEW",
                epic1.getId());
        taskManager.addSubTask(subTask1);
        //System.out.println(subTask1.getId()+subTask1.getName()+subTask1.getDescription()+subTask1.getStatus());


        SubTask subTask2 = new SubTask("скачать вопросник", "ссылка на сайте уника", 0,"DONE",
                epic1.getId());
        taskManager.addSubTask(subTask2);
        //System.out.println(subTask2.getId()+subTask2.getName()+subTask2.getDescription()+subTask2.getStatus());
        System.out.println(epic1.getStatus());

        Epic epic2 = new Epic("получить диплом", "через 6 месяцев", 0, "NEW");
        taskManager.addEpic(epic2);
        //System.out.println(epic2.getId()+epic2.getName()+epic2.getDescription()+epic2.getStatus());
        System.out.println(epic2.getStatus());

        SubTask subTask3 = new SubTask("купить прописи", "в буквоеде скидки", 0,"DONE",
                epic2.getId());
        taskManager.addSubTask(subTask3);
        //System.out.println(subTask3.getId()+subTask3.getName()+subTask3.getDescription()+subTask3.getStatus()
        // +subTask3.getEpicId());

        /*System.out.println(taskManager.simpleTask);
        System.out.println(taskManager.epics);
        System.out.println(taskManager.subTasks);*/

        System.out.println(taskManager.getListOfSimpleTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfsubTasks());
        //System.out.println(taskManager.simpleTask);

        //taskManager.getListOfSubsOfEpic(epic1);

        /*System.out.println(epic1.getSubsId());
        taskManager.deleteSubTaskById(subTask1.getId(),epic1);
        System.out.println(epic1.getSubsId());
        System.out.println(taskManager.subTasks);*/

        SimpleTask task3 = new SimpleTask("Собрать коробки", "Для переезда", task1.getId(), "DONE");
        taskManager.updateSimpleTask(task3);
        System.out.println(task3.getId()+task3.getName()+task3.getDescription()+task3.getStatus());


        SimpleTask task4 = new SimpleTask("Приготовить ужин", "Плов", task2.getId(), "NEW");
        taskManager.updateSimpleTask(task4);
        System.out.println(task4.getId()+task4.getName()+task4.getDescription()+task4.getStatus());


        SubTask subTask5 = new SubTask("купить учебник", "в буквоеде скидки", subTask1.getId(), "DONE",
                epic1.getId());
        taskManager.updateSubTask(subTask5);
        System.out.println(subTask5.getId()+subTask5.getName()+subTask5.getDescription()+subTask5.getStatus()
                +subTask5.getEpicId());

        SubTask subTask6 = new SubTask("скачать вопросник", "ссылка на сайте уника", subTask2.getId(),"DONE",
                epic1.getId());
        taskManager.updateSubTask(subTask6);
        System.out.println(subTask6.getId()+subTask6.getName()+subTask6.getDescription()+subTask6.getStatus()
                +subTask6.getEpicId());

        SubTask subTask4 = new SubTask("заниматься по часу в день", "с 15 до 16", subTask3.getId(),"NEW",
                epic2.getId());
        taskManager.updateSubTask(subTask4);
        System.out.println(subTask4.getId()+subTask4.getName()+subTask4.getDescription()+subTask4.getStatus()
                +subTask4.getEpicId());
        System.out.println(epic1.getStatus());
        System.out.println(epic2.getStatus());

        Epic epic5 = new Epic("сдать экзамен", "сессия в июне", epic1.getId(), "NEW");
        taskManager.updateEpic(epic5);

        Epic epic4 = new Epic("получить red диплом", "через 6 месяцев", epic2.getId(), "NEW");
        taskManager.updateEpic(epic4);



        System.out.println(taskManager.getListOfSimpleTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfsubTasks());



        System.out.println(taskManager.getListOfSubsOfEpic(epic1));
        taskManager.deleteAllSubTasks();

        System.out.println(taskManager.getListOfSubsOfEpic(epic1));
        System.out.println(epic5.getStatus());
        System.out.println(epic4.getStatus());

        /*System.out.println(taskManager.epics);
        System.out.println(taskManager.getSubTaskById(subTask4.getId()).getName());*/

        /*System.out.println(taskManager.epics);
        System.out.println(epic4.getSubsId());
        System.out.println(taskManager.subTasks);*/














    }
}
