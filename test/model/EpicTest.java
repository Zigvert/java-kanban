package test.model;

import model.task.Epic;
import model.task.Subtask;
import model.dictionary.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void testEpicsAreEqualIfIdsAreEqual() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");

        epic1.setId(1);
        epic2.setId(1);

        assertTrue(epic1.equals(epic2), "Эпики с одинаковыми ID должны быть равны.");
    }

    @Test
    void testRecalculateFields() {
        Epic epic = new Epic("Epic 1", "Description 1");

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 9, 0), 1, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW, Duration.ofHours(3), LocalDateTime.of(2024, 12, 16, 9, 30), 2, 1);

        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        epic.recalculateFields(List.of(subtask1, subtask2));

        assertEquals(Duration.ofHours(5), epic.getDuration(), "Продолжительность эпика должна быть правильной.");
        assertEquals(LocalDateTime.of(2024, 12, 16, 9, 0), epic.getStartTime(), "Время начала эпика должно быть минимальным временем начала среди подзадач.");
        assertEquals(LocalDateTime.of(2024, 12, 16, 12, 30), epic.getEndTime(), "Время окончания эпика должно быть максимальным временем окончания среди подзадач.");
    }

    @Test
    void testAddAndRemoveSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 9, 0), 1, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW, Duration.ofHours(3), LocalDateTime.of(2024, 12, 16, 9, 30), 2, 1);

        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        assertTrue(epic.getSubtasksId().contains(subtask1.getId()), "Эпик должен содержать подзадачу 1.");
        assertTrue(epic.getSubtasksId().contains(subtask2.getId()), "Эпик должен содержать подзадачу 2.");

        epic.removeSubtaskId(subtask1.getId());

        assertFalse(epic.getSubtasksId().contains(subtask1.getId()), "Эпик не должен содержать подзадачу 1 после её удаления.");
    }
}
