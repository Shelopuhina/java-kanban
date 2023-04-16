import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, SimpleTask> simpleTask = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubEpic> subEpics = new HashMap<>();
    private int nextId = 1;
    private int nextIdEpic = 1;
    private int nextIdSub = 1;

    public ArrayList getListOfSimpleTasks() {
         ArrayList<Object> allSimpleTasks = new ArrayList<>(simpleTask.values());
         return allSimpleTasks;

    }
    public ArrayList getListOfEpics() {
        ArrayList<Object> allEpics = new ArrayList<>(epics.values());
        return allEpics;
    }
    public ArrayList getListOfSubEpics() {
        ArrayList<Object> allSubEpics = new ArrayList<>(subEpics.values());
        return allSubEpics;
    }
    public void deleteAllSimpleTasks() {
        simpleTask.clear();
    }
    public void deleteAllEpics() {
        epics.clear();
        subEpics.clear();
    }
    public void deleteAllSubEpics() {
        subEpics.clear();
    }
    public SimpleTask getTaskById (int id) {
        return simpleTask.get(id);
    }
    public Epic getEpicById (int id) {
        return epics.get(id);
    }
    public SubEpic getSubEpicById(int id) {
        return subEpics.get(id);
    }
    public int addSimpleTask(SimpleTask task) {
        task.setId(nextId);
        nextId++;
        simpleTask.put(task.getId(), task);
        return task.getId();
    }
    public int addEpic(Epic epic) {
        epic.setId(nextIdEpic);
        nextIdEpic++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public int addSubEpic(SubEpic subEpic, Epic epic) {
        subEpic.setId(nextIdSub);
        nextIdSub++;
        subEpics.put(subEpic.getId(), subEpic);
        epic.getSubsId().add(subEpic.getId());
        subEpic.setEpicId(epic.getId());
        getEpicStatus(epic, epic.getSubsId());
        return subEpic.getId();
    }

    private String getEpicStatus(Epic epic, ArrayList<Integer> idSubsOfEpic) {
        String epicStatus = null;
        int countNew = 0;
        int countDone = 0;
        for (Integer integer : epic.getSubsId()) {
            if (idSubsOfEpic.isEmpty()) {
                epicStatus = "NEW";
            } else {
                boolean isStatusNEW = subEpics.get(integer).getStatus() == "NEW";
                boolean isStatusDone = subEpics.get(integer).getStatus() == "DONE";

                if (isStatusNEW) {
                    countNew++;
                    continue;
                } else if (isStatusDone) {
                    countDone++;
                    continue;
                }
            }
        }
        if (countNew == idSubsOfEpic.size()) {
            epicStatus = "NEW";
            epic.setStatus(epicStatus);
        } else if (countDone == idSubsOfEpic.size()) {
            epicStatus = "DONE";
            epic.setStatus(epicStatus);
        } else {
            epicStatus = "IN PROGRESS";
            epic.setStatus(epicStatus);
        }
        return epic.getStatus();
    }

    public void updateSimpleTask(SimpleTask task, SimpleTask task1) {
        simpleTask.remove(task.getId());
        task1.setId(task.getId());
        simpleTask.put(task1.getId(), task1);
    }

    public void updateEpic(Epic epic,Epic epic1) {
        epic1.setId(epic.getId());
        epics.remove(epic.getId());
        epic1.setSubsId(epic.getSubsId());
        epics.put(epic1.getId(), epic1);
        getEpicStatus(epic1, epic1.getSubsId());

    }

    public void updateSubEpic(SubEpic subEpic, SubEpic subEpic1, Epic epic) {
        subEpic1.setId(subEpic.getId());
        subEpics.remove(subEpic.getId());
        subEpics.put(subEpic1.getId(), subEpic1);//положили новый саб c прежним id
        subEpic1.setEpicId(epic.getId());
        Epic epicTask = epics.get(subEpic1.getEpicId());//достали нужный эпик
        getEpicStatus(epicTask, epicTask.getSubsId());//обновили статус эпика

    }
    public void deleteSimpleTaskById(int id) {
        simpleTask.remove(id);
    }
    public void deleteEpicById (int id) {
        for (Integer integer : epics.get(id).getSubsId()) {
            subEpics.remove(integer);
        }
        epics.get(id).getSubsId().clear();
        epics.remove(id);
    }
    public void deleteSubEpicById (int id, Epic epic) {
        int epId = subEpics.get(id).getEpicId();
        epics.get(epId).getSubsId().remove(id);
        subEpics.remove(id);
        getEpicStatus(epic, epic.getSubsId());
    }
    public void getListOfSubsOfEpic(Epic epic) {
        for (Integer integer : epic.getSubsId()) {
            System.out.println(subEpics.get(integer).getName());
        }
    }
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
        System.out.println(epic1.getId()+epic1.getName()+epic1.getDescription()+epic1.getStatus());

        SubEpic subEpic1 = new SubEpic("купить учебник", "в буквоеде скидки", 0,"NEW", 0);
        taskManager.addSubEpic(subEpic1,epic1);
        System.out.println(subEpic1.getId()+subEpic1.getName()+subEpic1.getDescription()+subEpic1.getStatus());

        SubEpic subEpic2 = new SubEpic("скачать вопросник", "ссылка на сайте уника", 0,"DONE", 0);
        taskManager.addSubEpic(subEpic2, epic1);
        System.out.println(subEpic2.getId()+subEpic2.getName()+subEpic2.getDescription()+subEpic2.getStatus());

        Epic epic2 = new Epic("получить диплом", "через 6 месяцев", 0, "NEW");
        taskManager.addEpic(epic2);
        System.out.println(epic2.getId()+epic2.getName()+epic2.getDescription()+epic2.getStatus());

        SubEpic subEpic3 = new SubEpic("купить прописи", "в буквоеде скидки", 0,"DONE", 0);
        taskManager.addSubEpic(subEpic3, epic2);
        System.out.println(subEpic3.getId()+subEpic3.getName()+subEpic3.getDescription()+subEpic3.getStatus()+subEpic3.getEpicId());

        /*System.out.println(taskManager.simpleTask);
        System.out.println(taskManager.epics);
        System.out.println(taskManager.subEpics);*/

        System.out.println(taskManager.getListOfSimpleTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubEpics());
        System.out.println(taskManager.simpleTask);

        //taskManager.getListOfSubsOfEpic(epic1);

        /*System.out.println(epic1.getSubsId());
        taskManager.deleteSubEpicById(subEpic1.getId(),epic1);
        System.out.println(epic1.getSubsId());
        System.out.println(taskManager.subEpics);*/

        SimpleTask task3 = new SimpleTask("Собрать коробки", "Для переезда", 0, "DONE");
        taskManager.updateSimpleTask(task1,task3);
        System.out.println(task3.getId()+task3.getName()+task3.getDescription()+task3.getStatus());


        SimpleTask task4 = new SimpleTask("Приготовить ужин", "Плов", 0, "DONE");
        taskManager.updateSimpleTask(task2, task4);
        System.out.println(task4.getId()+task4.getName()+task4.getDescription()+task4.getStatus());
        System.out.println(taskManager.simpleTask);


        SubEpic subEpic5 = new SubEpic("купить учебник", "в буквоеде скидки", 0,"DONE", 0);
        taskManager.updateSubEpic(subEpic1, subEpic5,epic1);
        System.out.println(subEpic5.getId()+subEpic5.getName()+subEpic5.getDescription()+subEpic5.getStatus()+subEpic5.getEpicId());

        SubEpic subEpic6 = new SubEpic("скачать вопросник", "ссылка на сайте уника", 0,"DONE", 0);
        taskManager.updateSubEpic(subEpic2, subEpic6,epic1);
        System.out.println(subEpic6.getId()+subEpic6.getName()+subEpic6.getDescription()+subEpic6.getStatus()+subEpic6.getEpicId());

        SubEpic subEpic4 = new SubEpic("заниматься по часу в день", "с 15 до 16", 0,"NEW", 0);
        taskManager.updateSubEpic(subEpic3, subEpic4,epic2);
        System.out.println(subEpic4.getId()+subEpic4.getName()+subEpic4.getDescription()+subEpic4.getStatus()+subEpic4.getEpicId());

        Epic epic4 = new Epic("получить red диплом", "через 6 месяцев", 0, "NEW");
        taskManager.updateEpic(epic2, epic4);
        System.out.println(epic4.getStatus());
        System.out.println(epic1.getStatus());

        System.out.println(taskManager.getListOfSimpleTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubEpics());

        taskManager.deleteEpicById(epic4.getId());
        System.out.println(taskManager.epics);
        System.out.println(epic4.getSubsId());
        System.out.println(taskManager.subEpics);

        taskManager.deleteSimpleTaskById(task1.getId());
        System.out.println(taskManager.getListOfSimpleTasks());

        //taskManager.deleteAllEpics();
        /*System.out.println(taskManager.epics);
        System.out.println(taskManager.getSubEpicById(subEpic4.getId()).getName());*/

        /*System.out.println(taskManager.epics);
        System.out.println(epic4.getSubsId());
        System.out.println(taskManager.subEpics);*/














    }

}
