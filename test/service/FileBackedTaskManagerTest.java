package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.dictionary.Status;


import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = new File("test_tasks.csv");
        if (file.exists()) {
            file.delete();
        }
        manager = new FileBackedTaskManager(file);
    }

    @Test
    void testSaveAndLoad() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.setTask(task1);
        manager.setTask(task2);

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        List<Task> loadedTasks = loadedManager.getAllTasks();
        assertEquals(2, loadedTasks.size());
        assertTrue(loadedTasks.contains(task1));
        assertTrue(loadedTasks.contains(task2));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task 1", "Description 1");
        manager.setTask(task);
        int taskId = task.getId();

        task.setStatus(Status.DONE);
        manager.updateTask(task);

        Task updatedTask = manager.getTaskById(taskId);
        assertNotNull(updatedTask);
        assertEquals(Status.DONE, updatedTask.getStatus());
    }

    @Test
    void testRemoveTask() {
        Task task = new Task("Task 1", "Description 1");
        manager.setTask(task);
        int taskId = task.getId();

        manager.removeTaskById(taskId);

        Task removedTask = manager.getTaskById(taskId);
        assertNull(removedTask);
    }

    @Test
    void testEpicAndSubtaskRelationship() {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.setTask(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask description", Status.NEW, 0, epic.getId());
        manager.setTask(subtask);

        assertEquals(1, epic.getSubtasksId().size());
        assertTrue(epic.getSubtasksId().contains(subtask.getId()));

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Epic loadedEpic = (Epic) loadedManager.getTaskById(epic.getId());
        Subtask loadedSubtask = (Subtask) loadedManager.getTaskById(subtask.getId());

        assertNotNull(loadedEpic);
        assertNotNull(loadedSubtask);
        assertTrue(loadedEpic.getSubtasksId().contains(loadedSubtask.getId()));
    }

    @Test
    void testClearTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.setTask(task1);
        manager.setTask(task2);

        manager.clearTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void testClearEpics() {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.setTask(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask description", Status.NEW, 0, epic.getId());
        manager.setTask(subtask);

        manager.clearEpics();
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    void testHistory() {
        Task task = new Task("Task 1", "Description 1");
        manager.setTask(task);
        manager.getTaskById(task.getId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }
}
