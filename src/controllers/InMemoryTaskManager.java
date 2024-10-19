package controllers;

import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;
import model.util.TypeTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private int taskCounter = 1;
    private int subtaskCounter = 1;
    private int epicCounter = 1;

    @Override
    public int getCounterId(TypeTask typeTask) {
        switch (typeTask) {
            case TASK:
                return taskCounter++;
            case SUBTASK:
                return subtaskCounter++;
            case EPIC:
                return epicCounter++;
            default:
                throw new IllegalArgumentException("Unknown TypeTask: " + typeTask);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public void setTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        subtasks.remove(id);
        epics.remove(id);
    }

    @Override
    public List<Subtask> getSubtaskEpic(Epic epic) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
    public void checkEpicStatus(int epicId) {
    }

    @Override
    public List<Task> getHistory() {

        return new ArrayList<>();
    }
}
