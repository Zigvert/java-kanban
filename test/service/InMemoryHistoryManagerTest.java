package test.service;

import model.task.Task;
import model.dictionary.Status;
import service.InMemoryHistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager target;

    @BeforeEach
    void init() {
        target = new InMemoryHistoryManager(10);
    }

    @Test
    public void testHistoryManager() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(1), 2);

        target.add(task1);
        target.add(task2);

        List<Task> history = target.getHistoryTask();

        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testAddDuplicateTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Задача 1", "Описание задачи 1 (обновленная)", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);

        target.add(task1);
        target.add(task2);

        List<Task> history = target.getHistoryTask();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void testRemoveTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(1), 2);

        target.add(task1);
        target.add(task2);
        target.remove(1);

        List<Task> history = target.getHistoryTask();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void testRemoveFromBeginning() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(1), 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, Duration.ofHours(3), LocalDateTime.now().plusDays(2), 3);

        target.add(task1);
        target.add(task2);
        target.add(task3);

        target.remove(1);

        List<Task> history = target.getHistoryTask();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    public void testRemoveFromMiddle() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(1), 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, Duration.ofHours(3), LocalDateTime.now().plusDays(2), 3);

        target.add(task1);
        target.add(task2);
        target.add(task3);

        target.remove(2);

        List<Task> history = target.getHistoryTask();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    public void testRemoveFromEnd() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(1), 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, Duration.ofHours(3), LocalDateTime.now().plusDays(2), 3);

        target.add(task1);
        target.add(task2);
        target.add(task3);

        target.remove(3);

        List<Task> history = target.getHistoryTask();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testOrderAfterMultipleRemovals() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(1), 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, Duration.ofHours(3), LocalDateTime.now().plusDays(2), 3);
        Task task4 = new Task("Задача 4", "Описание задачи 4", Status.NEW, Duration.ofHours(4), LocalDateTime.now().plusDays(3), 4);

        target.add(task1);
        target.add(task2);
        target.add(task3);
        target.add(task4);

        target.remove(2);
        target.remove(4);

        List<Task> history = target.getHistoryTask();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }
}
