package tests;

import model.tasks.Task;
import model.util.Status;
import model.util.TypeTask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {

        Task task = new Task("Задача 1", "Описание задачи 1", Status.NEW, 1);

        assertNotNull(task);
        assertEquals("Задача 1", task.getName());  // Исправлено название
        assertEquals("Описание задачи 1", task.getDescription());  // Исправлено описание
        assertEquals(Status.NEW, task.getStatus());  // Добавлена проверка статуса
        assertEquals(TypeTask.TASK, task.getTypeTask());  // Проверка типа задачи
        assertEquals(1, task.getId());  // Проверка ID
    }
}
