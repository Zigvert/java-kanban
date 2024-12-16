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

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,startTime,duration,epic\n");

            for (Task task : tasks.values()) {
                writer.write(TaskFileUtils.toString(task));
                writer.newLine();
            }

            for (Epic epic : epics.values()) {
                writer.write(TaskFileUtils.toString(epic));
                writer.newLine();
            }

            for (Subtask subtask : subtasks.values()) {
                writer.write(TaskFileUtils.toString(subtask));
                writer.newLine();
            }

            writer.flush();

        } catch (IOException e) {
            throw new ManagerSaveException("Error saving data to file: " + file.getAbsolutePath(), e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        if (!file.exists()) {
            return manager;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int maxId = 0;

            for (String line : lines) {
                if (line.isBlank() || line.startsWith("id,")) continue;

                Task task = TaskFileUtils.fromString(line);
                manager.setTask(task);
                maxId = Math.max(maxId, task.getId());
            }

            for (Task task : manager.getAllEpics()) {
                if (task instanceof Epic) {
                    Epic epic = (Epic) task;
                    List<Subtask> subtasks = manager.getSubtaskEpic(epic);
                    epic.recalculateFields(subtasks);
                }
            }

            manager.counterId = maxId + 1;

        } catch (IOException e) {
            throw new ManagerSaveException("Error loading data from file: " + file.getAbsolutePath(), e);
        }

        return manager;
    }
}
