package service;

import model.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev) {
            this.task = task;
            this.prev = prev;
        }
    }

    private Node head;
    private Node tail;
    private final Map<Integer, Node> taskMap = new HashMap<>();

    private final List<Task> historyTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        removeNode(taskMap.get(task.getId()));

        Node newNode = new Node(task, tail);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;

        taskMap.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        removeNode(taskMap.get(id));
    }

    @Override
    public List<Task> getHistoryTask() {
        List<Task> taskHistory = new ArrayList<>();
        Node current = head;
        while (current != null) {
            taskHistory.add(current.task);
            current = current.next;
        }
        return taskHistory;
    }

    private void removeNode(Node node) {
        if (node == null) return;

        taskMap.remove(node.task.getId());

        if (node == head) {
            head = node.next;
        }
        if (node == tail) {
            tail = node.prev;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
    }
}
