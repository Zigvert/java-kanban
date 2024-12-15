package util;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.TaskType;
import model.dictionary.Status;
import service.FileBackedTaskManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskFileUtils {

    public static Task fromString(String value) {
        String[] fields = value.split(",");
        if (fields.length < 5) {
            throw new IllegalArgumentException("Некорректный формат строки: " + value);
        }

        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case TASK:
                return new Task(name, description, status, id);
            case EPIC:
                return new Epic(name, description, id);
            case SUBTASK:
                if (fields.length < 6) {
                    throw new IllegalArgumentException("Недостаточно данных для подзадачи: " + value);
                }
                int epicId = Integer.parseInt(fields[5]);
                return new Subtask(name, description, status, id, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static String toString(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",").append(task.getTypeTask()).append(",")
                .append(task.getName()).append(",").append(task.getStatus()).append(",")
                .append(task.getDescription());

        if (task instanceof Subtask) {
            sb.append(",").append(((Subtask) task).getEpicId());
        }

        return sb.toString();
    }

    public static String historyToString(List<Task> history) {
        if (history == null) {
            throw new IllegalArgumentException("История не может быть null");
        }

        return history.stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    // Метод для восстановления истории
    public static List<Task> historyFromString(String value, FileBackedTaskManager manager) {
        if (value == null || value.isEmpty()) {
            return List.of();
        }

        String[] taskIds = value.split(",");
        return Arrays.stream(taskIds)
                .map(id -> manager.getTaskById(Integer.parseInt(id)))
                .collect(Collectors.toList());
    }
}
