package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList<Task> custom = new CustomLinkedList<>();
    private Map<Integer, Node<Task>> nodes = new HashMap<>();
    @Override
    public void addTask(Task task) {
        if (task == null) {
          return;
        }
        if(nodes.containsKey(task.getId())) {
            remove(task.getId());
            nodes.remove(task.getId());
        }
        custom.linkLast(task);
        nodes.put(task.getId(), custom.tail);
    }
    @Override
    public void remove (int id){
        custom.removeNode(nodes.get(id));
    }
    @Override
    public List<Task> getHistory () {
            return custom.getTasks();
        }
    }
    class CustomLinkedList<Task> {

        public Node<Task> head;
        public Node<Task> tail;
        private int size = 0;
        List<Task> history = new ArrayList<>();
        public void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
                newNode.prev = oldTail;
            }
            size++;
        }
        public List<Task> getTasks() {

            Node<Task> node1 = head;
            while (node1 != null) {
                history.add(node1.data);
                node1 = node1.next;
            }
            System.out.println(history);
            return history;
        }
        public void removeNode(Node node1) {
            if (node1 != null) {
                Node <Task> nextNode = node1.next;
                Node <Task> prevNode = node1.prev;

                if (prevNode == null) {
                    head = nextNode;
                } else {
                    prevNode.next = nextNode;
                }
                if (nextNode == null) {
                    tail = prevNode;
                } else {
                    nextNode.prev = prevNode;
                }
                size--;
            }
        }
    }



