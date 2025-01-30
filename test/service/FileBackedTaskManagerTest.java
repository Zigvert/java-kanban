package service;

import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    protected FileBackedTaskManager createTaskManager() {
        File file = new File("tasks.dat");
        return new FileBackedTaskManager(file);
    }

    @Test
    void testLoadFromFile() {

        File file = new File("tasks.dat");
        FileBackedTaskManager taskManager = createTaskManager();

        Task task = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        taskManager.setTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        Task loadedTask = loadedManager.getTaskById(1);
        assertEquals(task, loadedTask, "Задача должна быть загружена из файла по ID");
    }

    @Test
    void testLoadEmptyFile() {
        File file = new File("empty_tasks.dat");

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        assertNotNull(loadedManager, "Менеджер задач не должен быть null");
        assertTrue(loadedManager.getAllTasks().isEmpty(), "В загруженном менеджере не должно быть задач");
    }
}
