package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

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
    void testUpdateEpicStatusBasedOnSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2024, 12, 16, 9, 0), 1, epic.getId());
        taskManager.setTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.DONE, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 2, epic.getId());
        taskManager.setTask(subtask2);

        taskManager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subtask1.setStatus(Status.DONE);
        taskManager.updateTask(subtask1);

        taskManager.updateEpicStatus(epic);
        assertEquals(Status.DONE, epic.getStatus(), "Эпик должен иметь статус DONE");
    }

    @Test
    void testTaskOverlap() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 11, 0), 2);

        taskManager.setTask(task1);
        taskManager.setTask(task2);

        assertTrue(taskManager.isOverlap(task2), "Задачи должны пересекаться");
    }

    @Test
    void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(3), LocalDateTime.of(2024, 12, 16, 8, 0), 2);

        taskManager.setTask(task1);
        taskManager.setTask(task2);
        
        assertEquals(2, taskManager.getPrioritizedTasks().size(), "Количество приоритетных задач должно быть 2");
        assertEquals(task2, taskManager.getPrioritizedTasks().get(0), "Первая приоритетная задача должна быть task2");
        assertEquals(task1, taskManager.getPrioritizedTasks().get(1), "Вторая приоритетная задача должна быть task1");
    }
}
