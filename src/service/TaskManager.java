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

    boolean removeTaskById(int id);

    List<Subtask> getSubtaskEpic(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    void addSubtask(Subtask subtask);

    boolean removeSubtaskById(int id);

    boolean removeEpicById(int id);

}
