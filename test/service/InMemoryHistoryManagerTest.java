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

        // После добавления новой версии задачи в историю, должна остаться только последняя версия
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0)); // Проверка, что осталась обновленная версия задачи
    }

    @Test
    public void testRemoveTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 2);

        target.add(task1);
        target.add(task2);
        target.remove(1);  // Удаляем задачу по id

        List<Task> history = target.getHistoryTask();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0)); // После удаления task1, должна остаться только task2
    }
}
