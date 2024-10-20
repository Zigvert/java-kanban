package controllers;

import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;


import java.util.ArrayList;
import java.util.List;

public interface TaskManager {


    ArrayList<Task> getAllTasks();

    ArrayList<Task> getAllEpics();

    ArrayList<Task> getAllSubtasks();

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Task getTaskById(int id);

    void setTask(Task task);

    void updateTask(Task task);

    void removeTaskById(int id);

    ArrayList<Subtask> getSubtaskEpic(Epic epic);



    List<Task> getHistory();
}