package test.service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.setTask(task);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.setTask(task);

        task.setStatus(Status.DONE);
        taskManager.updateTask(task);

        assertEquals(Status.DONE, taskManager.getTaskById(task.getId()).getStatus());
    }

    @Test
    void testRemoveTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.setTask(task);
        int taskId = task.getId();

        taskManager.removeTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId));
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void testRemoveSubtaskFromEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1", Status.NEW, 1, epic.getId());
        taskManager.setTask(subtask);

        taskManager.removeTaskById(subtask.getId());

        // Проверка, что эпик больше не содержит удаленную подзадачу
        assertEquals(0, taskManager.getSubtaskEpic(epic).size());
    }

    @Test
    void testRemoveEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);
        int epicId = epic.getId();

        taskManager.removeTaskById(epicId);
        assertNull(taskManager.getTaskById(epicId));
        assertEquals(0, taskManager.getAllEpics().size());
    }
}
