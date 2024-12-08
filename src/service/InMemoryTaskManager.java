package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import model.dictionary.TaskType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int counterId = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = ManagerProvider.getDefaultHistory();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    private int generateId() {
        return counterId++;
    }

    private TaskType getTypeById(int id) {
        if (tasks.containsKey(id)) return TaskType.TASK;
        if (subtasks.containsKey(id)) return TaskType.SUBTASK;
        if (epics.containsKey(id)) return TaskType.EPIC;
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryTask();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            epics.get(subtask.getEpicId()).removeSubtaskId(subtask.getId());
        }

        subtasks.clear();
        epics.values().forEach(this::updateEpicStatus);
    }

    @Override
    public Task getTaskById(int id) {
        TaskType type = getTypeById(id);
        if (type == null) return null;

        Task task = switch (type) {
            case TASK -> tasks.get(id);
            case SUBTASK -> subtasks.get(id);
            case EPIC -> epics.get(id);
        };

        historyManager.add(task);
        return task;
    }

    @Override
    public void setTask(Task task) {
        task.setId(generateId());
        switch (task.getTypeTask()) {
            case TASK -> tasks.put(task.getId(), task);
            case SUBTASK -> {
                Subtask subtask = (Subtask) task;
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                if (epic != null) {
                    epic.setSubtasks(subtask);
                }
            }
            case EPIC -> epics.put(task.getId(), (Epic) task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (getTypeById(task.getId()) != task.getTypeTask()) {
            removeTaskById(task.getId());
        }

        switch (task.getTypeTask()) {
            case TASK -> tasks.put(task.getId(), task);
            case SUBTASK -> {
                subtasks.put(task.getId(), (Subtask) task);
                updateEpicStatus(epics.get(((Subtask) task).getEpicId()));
            }
            case EPIC -> {
                Epic epic = (Epic) task;
                epic.setSubtasksId(epics.get(task.getId()).getSubtasksId());
                epics.put(task.getId(), epic);
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public void removeTaskById(int id) {
        TaskType type = getTypeById(id);
        if (type == null) return;

        switch (type) {
            case TASK -> tasks.remove(id);
            case SUBTASK -> {
                Subtask subtask = subtasks.remove(id);
                Epic epic = epics.get(subtask.getEpicId());
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
            case EPIC -> {
                Epic epic = epics.remove(id);
                for (Integer subtaskId : epic.getSubtasksId()) {
                    subtasks.remove(subtaskId);
                }
                epic.getSubtasksId().clear();
            }
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskEpic(Epic epic) {
        ArrayList<Subtask> subtasksEpic = new ArrayList<>();
        for (Integer id : epic.getSubtasksId()) {
            subtasksEpic.add(subtasks.get(id));
        }
        return subtasksEpic;
    }

    private void updateEpicStatus(Epic epic) {
        int counterNew = 0;
        int counterDone = 0;
        ArrayList<Integer> subtaskIds = epic.getSubtasksId();

        for (Integer subtaskId : subtaskIds) {
            Status status = subtasks.get(subtaskId).getStatus();
            if (status == Status.NEW) counterNew++;
            else if (status == Status.DONE) counterDone++;
        }

        if (subtaskIds.isEmpty() || counterNew == subtaskIds.size()) {
            epic.setStatus(Status.NEW);
        } else if (counterDone == subtaskIds.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
