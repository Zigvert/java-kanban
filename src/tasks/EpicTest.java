package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("Test Epic", "Description for Test Epic");
    }

    @Test
    void testEpicCreation() {
        assertNotNull(epic);
        assertEquals("Test Epic", epic.getTitle());
        assertEquals("Description for Test Epic", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }
}
