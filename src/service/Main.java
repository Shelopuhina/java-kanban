package service;

import model.Epic;
import model.SimpleTask;
import model.SubEpic;

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


        SubEpic subEpic1 = new SubEpic("купить учебник", "в буквоеде скидки", 0,"NEW",
                epic1.getId());
        taskManager.addSubEpic(subEpic1);
        //System.out.println(subEpic1.getId()+subEpic1.getName()+subEpic1.getDescription()+subEpic1.getStatus());


        SubEpic subEpic2 = new SubEpic("скачать вопросник", "ссылка на сайте уника", 0,"DONE",
                epic1.getId());
        taskManager.addSubEpic(subEpic2);
        //System.out.println(subEpic2.getId()+subEpic2.getName()+subEpic2.getDescription()+subEpic2.getStatus());
        System.out.println(epic1.getStatus());

        Epic epic2 = new Epic("получить диплом", "через 6 месяцев", 0, "NEW");
        taskManager.addEpic(epic2);
        //System.out.println(epic2.getId()+epic2.getName()+epic2.getDescription()+epic2.getStatus());
        System.out.println(epic2.getStatus());

        SubEpic subEpic3 = new SubEpic("купить прописи", "в буквоеде скидки", 0,"DONE",
                epic2.getId());
        taskManager.addSubEpic(subEpic3);
        //System.out.println(subEpic3.getId()+subEpic3.getName()+subEpic3.getDescription()+subEpic3.getStatus()
        // +subEpic3.getEpicId());

        /*System.out.println(taskManager.simpleTask);
        System.out.println(taskManager.epics);
        System.out.println(taskManager.subEpics);*/

        System.out.println(taskManager.getListOfSimpleTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubEpics());
        //System.out.println(taskManager.simpleTask);

        //taskManager.getListOfSubsOfEpic(epic1);

        /*System.out.println(epic1.getSubsId());
        taskManager.deleteSubEpicById(subEpic1.getId(),epic1);
        System.out.println(epic1.getSubsId());
        System.out.println(taskManager.subEpics);*/

        SimpleTask task3 = new SimpleTask("Собрать коробки", "Для переезда", task1.getId(), "DONE");
        taskManager.updateSimpleTask(task3);
        System.out.println(task3.getId()+task3.getName()+task3.getDescription()+task3.getStatus());


        SimpleTask task4 = new SimpleTask("Приготовить ужин", "Плов", task2.getId(), "DONE");
        taskManager.updateSimpleTask(task4);
        System.out.println(task4.getId()+task4.getName()+task4.getDescription()+task4.getStatus());


        SubEpic subEpic5 = new SubEpic("купить учебник", "в буквоеде скидки", subEpic1.getId(), "DONE",
                epic1.getId());
        taskManager.updateSubEpic(subEpic5);
        System.out.println(subEpic5.getId()+subEpic5.getName()+subEpic5.getDescription()+subEpic5.getStatus()
                +subEpic5.getEpicId());

        SubEpic subEpic6 = new SubEpic("скачать вопросник", "ссылка на сайте уника", subEpic2.getId(),"NEW",
                epic1.getId());
        taskManager.updateSubEpic(subEpic6);
        System.out.println(subEpic6.getId()+subEpic6.getName()+subEpic6.getDescription()+subEpic6.getStatus()
                +subEpic6.getEpicId());

        SubEpic subEpic4 = new SubEpic("заниматься по часу в день", "с 15 до 16", subEpic3.getId(),"NEW",
                epic2.getId());
        taskManager.updateSubEpic(subEpic4);
        System.out.println(subEpic4.getId()+subEpic4.getName()+subEpic4.getDescription()+subEpic4.getStatus()
                +subEpic4.getEpicId());

        Epic epic5 = new Epic("сдать экзамен", "сессия в июне", epic1.getId(), "NEW");
        taskManager.updateEpic(epic5);

        Epic epic4 = new Epic("получить red диплом", "через 6 месяцев", epic2.getId(), "NEW");
        taskManager.updateEpic(epic4);

        System.out.println(epic5.getStatus());
        System.out.println(epic4.getStatus());

        System.out.println(taskManager.getListOfSimpleTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubEpics());

        taskManager.deleteSimpleTaskById(task1.getId());
        System.out.println(taskManager.getListOfSimpleTasks());

        System.out.println(taskManager.getListOfSubsOfEpic(epic1));
        taskManager.deleteAllSubEpics();

        System.out.println(taskManager.getListOfSubsOfEpic(epic1));
        System.out.println(epic5.getStatus());
        System.out.println(epic4.getStatus());

        /*System.out.println(taskManager.epics);
        System.out.println(taskManager.getSubEpicById(subEpic4.getId()).getName());*/

        /*System.out.println(taskManager.epics);
        System.out.println(epic4.getSubsId());
        System.out.println(taskManager.subEpics);*/














    }
}
