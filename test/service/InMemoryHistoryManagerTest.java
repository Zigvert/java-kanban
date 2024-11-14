package test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import model.task.Task;
import model.dictionary.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager target;

    @BeforeEach
    void init() {
        target = new InMemoryHistoryManager();
    }

    @Test
    public void testHistoryManager() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 2);

        target.add(task1);
        target.add(task2);

        List<Task> history = target.getHistoryTask();

        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testAddDuplicateTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 1", "Описание задачи 1 (обновленная)", Status.NEW, 1);

        target.add(task1);
        target.add(task2);

        List<Task> history = target.getHistoryTask();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void testRemoveTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 2);

        target.add(task1);
        target.add(task2);
        target.remove(1);

        List<Task> history = target.getHistoryTask();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void testRemoveFromBeginning() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, 3);

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
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, 3);

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
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, 3);

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
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 2);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, 3);
        Task task4 = new Task("Задача 4", "Описание задачи 4", Status.NEW, 4);

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
