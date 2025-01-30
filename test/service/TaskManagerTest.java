package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    void testAddEpicAndSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2024, 12, 16, 9, 0), 1, epic.getId());
        taskManager.setTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 2, epic.getId());
        taskManager.setTask(subtask2);

        assertEquals(2, taskManager.getSubtaskEpic(epic).size());
        assertTrue(taskManager.getSubtaskEpic(epic).contains(subtask1));
        assertTrue(taskManager.getSubtaskEpic(epic).contains(subtask2));

        epic.recalculateFields(taskManager.getSubtaskEpic(epic));

        assertEquals(Duration.ofHours(3), epic.getDuration());
        assertNotNull(epic.getStartTime());
        assertNotNull(epic.getEndTime());
    }


    @Test
    void testTaskOverlap() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 11, 0), 2);

        taskManager.setTask(task1);


        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.setTask(task2);
        });
    }


    @Test
    void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(3), LocalDateTime.of(2024, 12, 16, 13, 0), 2); // изменено время для отсутствия пересечений

        taskManager.setTask(task1);
        taskManager.setTask(task2);

        assertEquals(2, taskManager.getPrioritizedTasks().size(), "Количество приоритетных задач должно быть 2");

        assertEquals(task1, taskManager.getPrioritizedTasks().get(0), "Первая приоритетная задача должна быть task1");
        assertEquals(task2, taskManager.getPrioritizedTasks().get(1), "Вторая приоритетная задача должна быть task2");
    }

    @Test
    void testClearTasks() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        taskManager.setTask(task1);
        taskManager.clearTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задачи должны быть очищены");
    }

    @Test
    void testClearEpics() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.setTask(epic);
        taskManager.clearEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Эпики должны быть очищены");
    }

    @Test
    void testClearSubtasks() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.setTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, epic.getId());
        taskManager.setTask(subtask);
        taskManager.clearSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Подзадачи должны быть очищены");
    }

    @Test
    void testGetTaskById() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        taskManager.setTask(task);
        Task fetchedTask = taskManager.getTaskById(1);
        assertEquals(task, fetchedTask, "Задача должна быть получена по ID");
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        taskManager.setTask(task);


        Task updatedTask = new Task(task.getName(), "Updated Description", task.getStatus(), task.getDuration(), task.getStartTime(), task.getId());
        taskManager.updateTask(updatedTask);

        Task fetchedTask = taskManager.getTaskById(1);
        assertEquals("Updated Description", fetchedTask.getDescription(), "Описание задачи должно быть обновлено");
    }


    @Test
    void testRemoveTaskById() {
        Task task = new Task("Task 1", "Description", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        taskManager.setTask(task);
        taskManager.removeTaskById(1);
        assertNull(taskManager.getTaskById(1), "Задача должна быть удалена по ID");
    }

    @Test
    void testGetHistory() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusHours(1), 2);

        taskManager.setTask(task1);
        taskManager.setTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertTrue(history.contains(task1), "История должна содержать task1");
        assertTrue(history.contains(task2), "История должна содержать task2");
    }

}
