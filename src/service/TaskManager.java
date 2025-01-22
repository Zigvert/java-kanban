package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Task> getAllEpics();

    List<Task> getAllSubtasks();

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Task getTaskById(int id);

    void setTask(Task task);

    void updateTask(Task task);

    void removeTaskById(int id);

    List<Subtask> getSubtaskEpic(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean isOverlap(Task task);

}
