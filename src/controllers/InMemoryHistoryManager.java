package controllers;

import model.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> historyTask = new LinkedList<>();
    private final int CAPACITY = 10;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        historyTask.add(task);

        if (historyTask.size() > CAPACITY) {
            historyTask.removeFirst();
        }
    }

    @Override
    public List<Task> getHistoryTask() {
        return List.copyOf(historyTask);
    }
}
