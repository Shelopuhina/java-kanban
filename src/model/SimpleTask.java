package model;


import model.enums.TaskStatus;
import model.enums.TaskType;

import java.time.Instant;

public class SimpleTask extends Task {

      public SimpleTask(String name, String description, int id, TaskStatus status, long duration, Instant startTime) {
        super(name, description, id, status, TaskType.SIMPLE_TASK, duration, startTime);
      }

}
