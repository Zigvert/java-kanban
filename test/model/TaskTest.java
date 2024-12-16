package test.model;

import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTasksAreEqualIfIdsAreEqual() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);

        assertEquals(task1, task2);
    }

    @Test
    void testTasksAreNotEqualIfIdsAreDifferent() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 2);

        assertNotEquals(task1, task2);
    }

    @Test
    void testHashCodeIsEqualForSameIds() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);

        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testHashCodeIsNotEqualForDifferentIds() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 2);

        assertNotEquals(task1.hashCode(), task2.hashCode());
    }
}

