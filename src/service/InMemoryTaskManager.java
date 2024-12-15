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
    protected int counterId = 0;

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected final HistoryManager historyManager = ManagerProvider.getDefaultHistory();

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
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(subtask.getId());
            }
        }
        subtasks.clear();
        epics.values().forEach(this::updateEpicStatus);
    }

    @Override
    public Task getTaskById(int id) {
        TaskType type = getTypeById(id);
        if (type == null) {
            return null;
        }

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
            case EPIC -> epics.put(task.getId(), (Epic) task);
            case SUBTASK -> {
                Subtask subtask = (Subtask) task;
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                if (epic != null) {
                    epic.addSubtaskId(subtask.getId());
                    updateEpicStatus(epic);
                }
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        TaskType currentType = getTypeById(task.getId());
        if (currentType == null || currentType != task.getTypeTask()) {
            removeTaskById(task.getId());
        }

        switch (task.getTypeTask()) {
            case TASK -> tasks.put(task.getId(), task);
            case EPIC -> {
                Epic epic = (Epic) task;
                epics.put(task.getId(), epic);
                updateEpicStatus(epic);
            }
            case SUBTASK -> {
                Subtask subtask = (Subtask) task;
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epics.get(subtask.getEpicId()));
            }
        }
    }

    @Override
    public void removeTaskById(int id) {
        TaskType type = getTypeById(id);
        if (type == null) {
            return;
        }

        switch (type) {
            case TASK -> tasks.remove(id);
            case EPIC -> {
                Epic epic = epics.remove(id);
                if (epic != null) {
                    for (Integer subtaskId : epic.getSubtasksId()) {
                        subtasks.remove(subtaskId);
                    }
                }
            }
            case SUBTASK -> {
                Subtask subtask = subtasks.remove(id);
                if (subtask != null) {
                    Epic epic = epics.get(subtask.getEpicId());
                    if (epic != null) {
                        epic.removeSubtaskId(id);
                        updateEpicStatus(epic);
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskEpic(Epic epic) {
        if (epic == null) {
            return new ArrayList<>();
        }

        ArrayList<Subtask> subtasksEpic = new ArrayList<>();
        for (Integer id : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                subtasksEpic.add(subtask);
            }
        }
        return subtasksEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryTask();
    }

    public void synchronizeCounterId(int maxId) {
        this.counterId = maxId + 1;
    }

    public int generateId() {
        return counterId++;
    }

    private TaskType getTypeById(int id) {
        if (tasks.containsKey(id)) return TaskType.TASK;
        if (subtasks.containsKey(id)) return TaskType.SUBTASK;
        if (epics.containsKey(id)) return TaskType.EPIC;
        return null;
    }

    private void updateEpicStatus(Epic epic) {
        if (epic == null) {
            return;
        }

        int newCount = 0;
        int doneCount = 0;
        for (Integer id : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                Status status = subtask.getStatus();
                if (status == Status.NEW) {
                    newCount++;
                } else if (status == Status.DONE) {
                    doneCount++;
                }
            }
        }

        if (newCount == epic.getSubtasksId().size()) {
            epic.setStatus(Status.NEW);
        } else if (doneCount == epic.getSubtasksId().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
