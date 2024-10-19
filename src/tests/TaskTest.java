package tests;

import model.tasks.Task;
import model.util.TypeTask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        // Создание задачи с необходимыми параметрами
        Task task = new Task("Задача 1", "Описание задачи 1", TypeTask.TASK);

        assertNotNull(task);
        assertEquals("Задача 1", task.getName());
        assertEquals("Описание задачи 1", task.getDescription());
        assertEquals(TypeTask.TASK, task.getTypeTask());
    }

    // Добавьте другие тесты по мере необходимости
}
