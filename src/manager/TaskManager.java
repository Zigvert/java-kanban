package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int idCounter = 1;

    public Task createTask(String title, String description) {
        Task task = new Task(idCounter++, title, description, TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task;
    }

    public Subtask createSubtask(String title, String description, int epicId) {
        Subtask subtask = new Subtask(idCounter++, title, description, TaskStatus.NEW, epicId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.addSubtask(subtask.getId());
        }
        return subtask;
    }

    public Epic createEpic(String title, String description) {
        Epic epic = new Epic(idCounter++, title, description);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    public void deleteAllTasks() { tasks.clear(); }
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            epic.updateStatus(this);
        }
    }
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int id) { return tasks.get(id); }
    public Subtask getSubtaskById(int id) { return subtasks.get(id); }
    public Epic getEpicById(int id) { return epics.get(id); }

    public List<Task> getAllTasks() { return new ArrayList<>(tasks.values()); }
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(subtasks.values()); }
    public List<Epic> getAllEpics() { return new ArrayList<>(epics.values()); }
}

