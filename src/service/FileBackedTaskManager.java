package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import util.TaskFileUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void setTask(Task task) {
        super.setTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks().values()) {
                writer.write(TaskFileUtils.toString(task));
                writer.newLine();
            }
            for (Epic epic : getEpics().values()) {
                writer.write(TaskFileUtils.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : getSubtasks().values()) {
                writer.write(TaskFileUtils.toString(subtask));
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
            int taskId = 1;
            int epicId = 1;
            int subtaskId = 1;

            for (int i = 1; i < lines.size(); i++) {
                Task task = TaskFileUtils.fromString(lines.get(i));
                switch (task.getTypeTask()) {
                    case TASK -> {
                        task.setId(taskId++);
                        manager.getTasks().put(task.getId(), task);
                    }
                    case EPIC -> {
                        task.setId(epicId++);
                        manager.getEpics().put(task.getId(), (Epic) task);
                    }
                    case SUBTASK -> {
                        Subtask subtask = (Subtask) task;
                        subtask.setId(subtaskId++);
                        manager.getSubtasks().put(subtask.getId(), subtask);
                        // Добавляем подзадачу в эпик
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
}
