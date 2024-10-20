package tests;

import model.tasks.Task;
import model.util.Status;
import model.util.TypeTask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {

        Task task = new Task("������ 1", "�������� ������ 1", Status.NEW, 1);

        assertNotNull(task);
        assertEquals("������ 1", task.getName());  // ���������� ��������
        assertEquals("�������� ������ 1", task.getDescription());  // ���������� ��������
        assertEquals(Status.NEW, task.getStatus());  // ��������� �������� �������
        assertEquals(TypeTask.TASK, task.getTypeTask());  // �������� ���� ������
        assertEquals(1, task.getId());  // �������� ID
    }
}
