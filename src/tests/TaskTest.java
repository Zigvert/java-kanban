package tests;

import org.junit.jupiter.api.Test;
import model.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTasksAreEqualIfIdsAreEqual() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }
}
