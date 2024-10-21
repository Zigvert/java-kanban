package service;

import model.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistoryTask();
}