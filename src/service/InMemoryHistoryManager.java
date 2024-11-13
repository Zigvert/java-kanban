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

        public Node(Task task) {
            this.task = task;
        }
    }

    private Node head;
    private Node tail;
    private final Map<Integer, Node> taskMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }


        if (taskMap.containsKey(task.getId())) {
            removeNode(taskMap.get(task.getId()));
        }


        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        // Обновляем HashMap
        taskMap.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        Node node = taskMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistoryTask() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) return;


        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }


        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }
}

