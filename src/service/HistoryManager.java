package service;

import model.task.Task;

import java.util.List;

public interface HistoryManager {

    void clearHistory();

    void add(Task task);

    void remove(int id);

    List<Task> getHistoryTask();

}
