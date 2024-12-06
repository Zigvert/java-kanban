package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class FileBackedTaskManagerTest {

    @Test
    public void testLoadAndSave() {
        File tempFile = new File("tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task = new Task("Test Task", "Description");
        manager.addTask(task);

        Epic epic = new Epic("Epic Task", "Epic Description");
        manager.addTask(epic);

        Subtask subtask = new Subtask("Subtask", "Subtask Description", Status.NEW, 1, epic.getId());
        manager.addTask(subtask);

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(manager.getTasks(), loadedManager.getTasks());

        assertEquals(manager.getEpics(), loadedManager.getEpics());

        assertEquals(manager.getSubtasks(), loadedManager.getSubtasks());
    }
}
