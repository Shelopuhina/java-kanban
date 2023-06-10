package model;


import service.TaskType;

public class SimpleTask extends Task {

      public SimpleTask(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status, TaskType.SIMPLE_TASK);
      }
}
