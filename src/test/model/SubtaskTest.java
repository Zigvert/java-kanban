package test.model;

import model.task.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void testSubtasksAreEqualIfIdsAreEqual() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1");
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2");

        subtask1.setId(1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2);
    }

    @Test
    void testSubtasksAreNotEqualIfIdsAreDifferent() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1");
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2");

        subtask1.setId(1);
        subtask2.setId(2);

        assertNotEquals(subtask1, subtask2);
    }
}
