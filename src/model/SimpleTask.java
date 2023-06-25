package model;


import service.TaskType;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

public class SimpleTask extends Task {

      public SimpleTask(String name, String description, int id, TaskStatus status, long duration, Instant startTime) {
        super(name, description, id, status, TaskType.SIMPLE_TASK, duration, startTime);
      }
}
