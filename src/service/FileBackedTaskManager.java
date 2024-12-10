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
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");

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

            writer.newLine();
            writer.write(TaskFileUtils.historyToString(historyManager.getHistoryTask()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных в файл: " + file.getAbsolutePath(), e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int maxId = 0;

            for (String line : lines) {
                if (line.isBlank() || line.startsWith("id,")) continue;

                Task task = TaskFileUtils.fromString(line);
                manager.setTask(task);
                maxId = Math.max(maxId, task.getId());
            }

            for (Task task : manager.getAllTasks()) {
                if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    Epic epic = (Epic) manager.getTaskById(subtask.getEpicId());
                    if (epic != null) {
                        epic.addSubtask(subtask);
                    }
                }
            }

            String historyString = lines.get(lines.size() - 1);
            manager.setHistory(TaskFileUtils.historyFromString(historyString, manager));

            manager.synchronizeCounterId(maxId);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла: " + file.getAbsolutePath(), e);
        }
        return manager;
    }

    public void setHistory(List<Task> history) {
        historyManager.setHistory(history);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
