package service;

import model.task.Task;
import model.dictionary.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.io.File;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    void testAddTask() {
        File file = new File("tasks.dat");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        Task task = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        taskManager.setTask(task);

        Task fetchedTask = taskManager.getTaskById(1);
        assertEquals(task, fetchedTask, "Задача должна быть получена по ID");
    }

    @Test
    void testLoadFromFile() {
        File file = new File("tasks.dat");
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

        assertNotNull(taskManager, "Менеджер задач не должен быть null");
        assertTrue(taskManager.getAllTasks().size() > 0, "Должны быть загружены задачи");
    }

}
