package tests;

import model.tasks.Subtask;
import model.util.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    public void testSubtaskCreation() {

        Subtask subtask1 = new Subtask("��������� 1", "�������� ��������� 1", Status.NEW, 1, 1);
        Subtask subtask2 = new Subtask("��������� 2", "�������� ��������� 2", Status.NEW, 2, 1);

        assertNotNull(subtask1);
        assertNotNull(subtask2);
        assertEquals("��������� 1", subtask1.getName());
        assertEquals("��������� 2", subtask2.getName());
    }


}
