package test.service;

import org.junit.jupiter.api.BeforeEach;
import service.InMemoryHistoryManager;
import model.task.Task;
import model.dictionary.Status;

import org.junit.jupiter.api.Test;

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

}