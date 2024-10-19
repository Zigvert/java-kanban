package tests;

import controllers.InMemoryHistoryManager;
import model.tasks.Task;
import model.tasks.Subtask;
import model.util.Status;
import model.util.TypeTask;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    @Test
    public void testHistoryManager() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


        Task task1 = new Task("Задача 1", "Описание задачи 1", TypeTask.TASK);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TypeTask.TASK);


        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistoryTask();

        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testSubtaskCreation() {

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, 1, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, 2, 1);

        assertNotNull(subtask1);
        assertNotNull(subtask2);
        assertEquals("Подзадача 1", subtask1.getName());
        assertEquals("Подзадача 2", subtask2.getName());
    }


}
