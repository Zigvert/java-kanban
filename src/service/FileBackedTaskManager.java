package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import util.TaskFileUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

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
    public boolean removeTaskById(int id) {
        super.removeTaskById(id);
        save();
        return false;
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
                switch (task.getTypeTask()) {
                    case TASK -> {
                        manager.setTask(task);
                        maxId = Math.max(maxId, task.getId());
                    }
                    case SUBTASK -> {
                        manager.setTask(task);
                        Subtask subtask = (Subtask) task;
                        Epic epic = manager.epics.get(subtask.getEpicId());
                        if (epic != null) {
                            epic.addSubtaskId(subtask.getId());
                        }
                        maxId = Math.max(maxId, subtask.getId());
                    }
                    case EPIC -> {
                        manager.setTask(task);
                        maxId = Math.max(maxId, task.getId());
                    }
                }
            }

            manager.counterId = maxId + 1;

        } catch (IOException e) {
            throw new ManagerSaveException("Error loading data from file: " + file.getAbsolutePath(), e);
        }

        return manager;
    }
}
