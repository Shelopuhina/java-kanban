package service;

import model.Task;

public class Node<Task> {

public Task data;
public Node<Task> next;
public Node<Task> prev;

public Node(Node<Task> prev, Task task,Node<Task> next) {
        this.data = task;
        this.next = next;
        this.prev = prev;
        }
        }

