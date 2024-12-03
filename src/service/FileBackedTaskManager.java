package service;

import exception.ManagerSaveException;
import model.task.Epic;
import model.dictionary.Status;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    public void addTask(Task task) {

        if (task.getTypeTask() == TaskType.TASK) {
            getTasks().put(task.getId(), task);
        } else if (task.getTypeTask() == TaskType.EPIC) {
            getEpics().put(task.getId(), (Epic) task);
        } else if (task.getTypeTask() == TaskType.SUBTASK) {
            getSubtasks().put(task.getId(), (Subtask) task);
        }
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks().values()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : getEpics().values()) {
                writer.write(toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : getSubtasks().values()) {
                writer.write(toString(subtask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных в файл: " + file.getAbsolutePath(), e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                Task task = fromString(lines.get(i));
                switch (task.getTypeTask()) {
                    case TASK -> manager.getTasks().put(task.getId(), task);
                    case EPIC -> manager.getEpics().put(task.getId(), (Epic) task);
                    case SUBTASK -> {
                        Subtask subtask = (Subtask) task;
                        manager.getSubtasks().put(task.getId(), subtask);
                        Epic epic = manager.getEpics().get(subtask.getEpicId());
                        if (epic != null) {
                            epic.setSubtasks(subtask);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла: " + file.getAbsolutePath(), e);
        }
        return manager;
    }

    private static Task fromString(String value) {
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
                Subtask subtask = new Subtask(name, description, status, id, epicId);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + (task instanceof Subtask ? "," + ((Subtask) task).getEpicId() : "");
    }
}
