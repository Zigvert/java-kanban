package service;

import model.task.Task;
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

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(manager.getTasks().size(), loadedManager.getTasks().size());
        assertTrue(loadedManager.getTasks().containsKey(task.getId()));
    }
}

