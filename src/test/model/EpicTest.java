package test.model;

import model.task.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void testEpicsAreEqualIfIdsAreEqual() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2);
    }
}