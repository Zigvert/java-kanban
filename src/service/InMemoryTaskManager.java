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

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(new TaskComparator());

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
        Task task = getTaskByIdAndType(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void setTask(Task task) {
        if (task.getId() == 0) {
            task.setId(generateId());
        }


        if (task.getStartTime() != null) {

            if (task.getId() != 0) {
                prioritizedTasks.remove(task);
            }
            prioritizedTasks.add(task);
        }

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


        if (task.getStartTime() != null) {

            prioritizedTasks.remove(task);
            prioritizedTasks.add(task);
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

        Task taskToRemove = switch (type) {
            case TASK -> tasks.remove(id);
            case EPIC -> epics.remove(id);
            case SUBTASK -> subtasks.remove(id);
        };


        if (taskToRemove != null && taskToRemove.getStartTime() != null) {
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


    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private TaskType getTypeById(int id) {
        if (tasks.containsKey(id)) return TaskType.TASK;
        if (subtasks.containsKey(id)) return TaskType.SUBTASK;
        if (epics.containsKey(id)) return TaskType.EPIC;
        return null;
    }

    @Override
    public Task getTaskByIdAndType(int id) {
        Task task = tasks.get(id);
        if (task != null) return task;

        task = subtasks.get(id);
        if (task != null) return task;

        return epics.get(id);
    }

    public void updateEpicStatus(Epic epic) {
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
        }
    }

    public boolean isOverlap(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) {
            return false;
        }

        for (Task existingTask : prioritizedTasks) {
            if (existingTask.getStartTime() == null || existingTask.getDuration() == null) {
                continue;
            }

            LocalDateTime taskEndTime = task.getEndTime();
            LocalDateTime existingTaskEndTime = existingTask.getEndTime();


            if (!(existingTaskEndTime.isBefore(task.getStartTime()) || existingTask.getStartTime().isAfter(taskEndTime))) {
                return true;
            }
        }
        return false;
    }

    private void validateTaskOverlap(Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) {
            for (Task existingTask : prioritizedTasks) {

                if (existingTask.getStartTime() == null || existingTask.getEndTime() == null) {
                    continue;
                }


                System.out.println("Проверка пересечения: текущая задача " + task.getId() +
                        " с startTime=" + task.getStartTime() + " и endTime=" + task.getEndTime() +
                        " с задачей " + existingTask.getId() +
                        " с startTime=" + existingTask.getStartTime() + " и endTime=" + existingTask.getEndTime());


                if (!(existingTask.getEndTime().isBefore(task.getStartTime()) || existingTask.getStartTime().isEqual(task.getEndTime()) || existingTask.getStartTime().isAfter(task.getEndTime()))) {
                    System.out.println("Ошибка: Задача " + task.getId() + " пересекается с задачей " + existingTask.getId());
                    throw new IllegalArgumentException("Задача пересекается с другой задачей.");
                }
            }
        }
    }

    private int generateId() {
        return counterId++;
    }
}
