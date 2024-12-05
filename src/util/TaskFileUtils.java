package util;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.TaskType;

public class TaskUtils {

    public static String toString(Task task) {
        return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() +
                (task instanceof Subtask ? "," + ((Subtask) task).getEpicId() : "");
    }

    public static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case TASK:
                return new Task(name, description, status, id);
            case EPIC:
                return new Epic(name, description);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                return new Subtask(name, description, status, id, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}