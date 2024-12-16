package util;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.TaskType;
import model.dictionary.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TaskFileUtils {


    public static Task fromString(String value) {
        String[] fields = value.split(",");
        if (fields.length < 7) {
            throw new IllegalArgumentException("Некорректный формат строки: " + value);
        }

        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];


        LocalDateTime startTime = fields[5].isEmpty() ? null : LocalDateTime.parse(fields[5]);
        Duration duration = fields[6].isEmpty() ? Duration.ZERO : Duration.ofMinutes(Long.parseLong(fields[6]));


        switch (type) {
            case TASK:
                return new Task(name, description, status, duration, startTime, id);
            case EPIC:
                Epic epic = new Epic(name, description, id);

                if (fields.length > 7) {
                    List<Integer> subtasksId = List.of(fields[7].split(";")).stream()
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    epic.setSubtasksId(subtasksId);
                }
                return epic;
            case SUBTASK:
                if (fields.length < 8) {
                    throw new IllegalArgumentException("Недостаточно данных для подзадачи: " + value);
                }
                int epicId = Integer.parseInt(fields[7]);
                return new Subtask(name, description, status, duration, startTime, id, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static String toString(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",")
                .append(task.getTypeTask()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",");

        if (task.getStartTime() != null) {
            sb.append(task.getStartTime()).append(",");
        } else {
            sb.append(",");
        }

        if (task.getDuration() != null) {
            sb.append(task.getDuration().toMinutes()).append(",");
        } else {
            sb.append(",");
        }

        if (task.getTypeTask() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            sb.append(subtask.getEpicId());
        }

        if (task.getTypeTask() == TaskType.EPIC) {
            Epic epic = (Epic) task;
            String subtasks = epic.getSubtasksId().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(";"));
            sb.append(subtasks);
        }

        return sb.toString();
    }
}
