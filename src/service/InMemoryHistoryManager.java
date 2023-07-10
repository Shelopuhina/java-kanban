package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> custom = new CustomLinkedList<>();
    private final Map<Integer, Node<Task>> nodes = new HashMap<>();

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        if (nodes.containsKey(task.getId())) {
            remove(task.getId());
            nodes.remove(task.getId());
        }
        custom.linkLast(task);
        nodes.put(task.getId(), custom.getTail());
    }

    @Override
    public void remove(int id) {
        custom.removeNode(nodes.get(id));
        nodes.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return custom.getTasks();
    }


    private static class CustomLinkedList<Task> {
        private Node<Task> head;
        private Node<Task> tail;

        public Node<Task> getTail() {
            return tail;
        }

        private void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
        }

        private List<Task> getTasks() {
            List<Task> history = new ArrayList<>();
            Node<Task> node1 = head;
            while (node1 != null) {
                history.add(node1.data);
                node1 = node1.next;
            }
            return history;
        }

        private void removeNode(Node currentNode) {
            if (currentNode != null) {
                Node<Task> nextNode = currentNode.next;
                Node<Task> prevNode = currentNode.prev;

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
            }
        }
    }
}



