package model;

import service.EpicStatus;

public class SimpleTask extends Task {

      public SimpleTask(String name, String description, int id, EpicStatus status) {
        super(name, description, id, status);
      }
}
