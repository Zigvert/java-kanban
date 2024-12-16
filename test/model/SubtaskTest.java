package test.model;

import model.dictionary.Status;
import model.task.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    void testEqualsIfIdsAndEpicIdsAreEqual() {
        Subtask subtask1 = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);
        Subtask subtask2 = new Subtask("Task 2", "Desc 2", Status.IN_PROGRESS, Duration.ofHours(1), LocalDateTime.now(), 1, 10);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковыми id и epicId должны быть равны.");
    }

    @Test
    void testNotEqualsIfEpicIdsAreDifferent() {
        Subtask subtask1 = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);
        Subtask subtask2 = new Subtask("Task 2", "Desc 2", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 20);

        assertNotEquals(subtask1, subtask2, "Подзадачи с одинаковыми id, но разными epicId не должны быть равны.");
    }

    @Test
    void testNotEqualsIfIdsAreDifferent() {
        Subtask subtask1 = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);
        Subtask subtask2 = new Subtask("Task 2", "Desc 2", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 2, 10);

        assertNotEquals(subtask1, subtask2, "Подзадачи с разными id не должны быть равны.");
    }

    @Test
    void testEqualsIfAllAttributesAreSame() {
        Subtask subtask1 = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);
        Subtask subtask2 = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковыми всеми атрибутами должны быть равны.");
    }

    @Test
    void testHashCodeConsistency() {
        Subtask subtask1 = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);
        Subtask subtask2 = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);

        assertEquals(subtask1.hashCode(), subtask2.hashCode(), "Хэши одинаковых подзадач должны совпадать.");
    }

    @Test
    void testToString() {
        Subtask subtask = new Subtask("Task 1", "Desc 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now(), 1, 10);
        String expected = "Subtask{name='Task 1', description='Desc 1', id=1, status=NEW, startTime=" + subtask.getStartTime() + ", duration=PT1H, epicId=10}";

        assertEquals(expected, subtask.toString(), "Метод toString должен возвращать корректную строку.");
    }
}
