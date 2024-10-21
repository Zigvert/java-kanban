package test.service;

import service.InMemoryTaskManager;
import service.TaskManager;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testGetAllTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        taskManager.setTask(task1);
        taskManager.setTask(task2);

        assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test
    void testClearTasks() {
        taskManager.setTask(new Task("Task 1", "Description 1"));
        taskManager.setTask(new Task("Task 2", "Description 2"));

        taskManager.clearTasks();
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void testAddEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);

        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(epic, taskManager.getAllEpics().get(0));
    }

    @Test
    void testAddSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1", Status.NEW, 1, epic.getId());
        taskManager.setTask(subtask);

        assertEquals(1, taskManager.getSubtaskEpic(epic).size());
        assertEquals(subtask, taskManager.getSubtaskEpic(epic).get(0));
        assertEquals(subtask, taskManager.getTaskById(subtask.getId()));
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

    @Test
    void testClearEpics() {
        taskManager.setTask(new Epic("Epic 1", "Description 1"));
        taskManager.setTask(new Epic("Epic 2", "Description 2"));

        taskManager.clearEpics();
        assertEquals(0, taskManager.getAllEpics().size());
    }


    @Test
    void testRemoveSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);


        Subtask subtask = new Subtask("Subtask 1", "Description 1", Status.NEW, 1, epic.getId());
        taskManager.setTask(subtask);

        taskManager.removeTaskById(subtask.getId());
        assertNull(taskManager.getTaskById(subtask.getId()));
        assertEquals(0, taskManager.getSubtaskEpic(epic).size());
    }
}
