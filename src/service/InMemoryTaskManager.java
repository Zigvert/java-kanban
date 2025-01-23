package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import model.dictionary.TaskType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected int counterId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = ManagerProvider.getDefaultHistory();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Task::getId)
    );

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearTasks() {

        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
        }


        tasks.clear();
    }


    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();


        prioritizedTasks.removeIf(task -> task.getTypeTask() == TaskType.SUBTASK);
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
        prioritizedTasks.removeIf(task -> task.getTypeTask() == TaskType.SUBTASK);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = getTaskByIdAndType(id);
        historyManager.add(task);
        return task;
    }


    @Override
    public void setTask(Task task) {
        if (task.getId() == 0) {
            task.setId(generateId());
        }

        Task oldTask = getTaskByIdAndType(task.getId());
        if (oldTask != null) {
            prioritizedTasks.remove(oldTask);
        }


        if (task.getTypeTask() != TaskType.EPIC && isOverlap(task)) {
            throw new IllegalArgumentException("Task overlaps with an existing task.");
        }

        prioritizedTasks.add(task);
        historyManager.add(task);

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
        TaskType type = getTypeById(task.getId());
        if (type == null) { // Проверяем, существует ли задача
            throw new IllegalArgumentException("Task with the given ID does not exist.");
        }

        setTask(task);
    }


    @Override
    public void removeTaskById(int id) {
        TaskType type = getTypeById(id);
        if (type == null) {
            return;
        }
        Task taskToRemove = switch (type) {
            case TASK -> tasks.remove(id);
            case EPIC -> {
                Epic removedEpic = epics.remove(id);
                if (removedEpic != null) {
                    for (Integer subtaskId : removedEpic.getSubtasksId()) {
                        subtasks.remove(subtaskId);
                    }
                }
                yield removedEpic;
            }
            case SUBTASK -> subtasks.remove(id);
        };
        if (taskToRemove != null) {
            prioritizedTasks.remove(taskToRemove);
        }
    }

    @Override
    public List<Subtask> getSubtaskEpic(Epic epic) {
        if (epic == null) {
            return new ArrayList<>();
        }
        return epic.getSubtasksId().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryTask();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private TaskType getTypeById(int id) {
        if (tasks.containsKey(id)) return TaskType.TASK;
        if (subtasks.containsKey(id)) return TaskType.SUBTASK;
        if (epics.containsKey(id)) return TaskType.EPIC;
        return null;
    }

    private Task getTaskByIdAndType(int id) {
        Task task = tasks.get(id);
        if (task != null) return task;
        task = subtasks.get(id);
        if (task != null) return task;
        return epics.get(id);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic == null) return;

        boolean hasDone = false;
        boolean hasNew = false;

        for (Integer subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                Status status = subtask.getStatus();
                if (status == Status.DONE) hasDone = true;
                if (status == Status.NEW) hasNew = true;
            }
        }

        if (hasDone && hasNew) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (hasDone) {
            epic.setStatus(Status.DONE);
        } else if (hasNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    private boolean isOverlap(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) {
            return false;
        }

        LocalDateTime taskStartTime = task.getStartTime();
        LocalDateTime taskEndTime = task.getEndTime();

        for (Task existingTask : prioritizedTasks) {
            if (existingTask.getId() == task.getId()) {
                continue;
            }

            if (existingTask.getStartTime() == null || existingTask.getDuration() == null) {
                continue;
            }

            LocalDateTime existingStartTime = existingTask.getStartTime();
            LocalDateTime existingEndTime = existingTask.getEndTime();

            if (taskStartTime.isBefore(existingEndTime) && taskEndTime.isAfter(existingStartTime)) {
                return true;
            }
        }

        return false;
    }

    private int generateId() {
        return ++counterId;
    }
}
