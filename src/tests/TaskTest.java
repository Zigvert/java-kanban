package tests;

import model.tasks.Task;
import model.util.TypeTask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        // �������� ������ � ������������ �����������
        Task task = new Task("������ 1", "�������� ������ 1", TypeTask.TASK);

        assertNotNull(task);
        assertEquals("������ 1", task.getName());
        assertEquals("�������� ������ 1", task.getDescription());
        assertEquals(TypeTask.TASK, task.getTypeTask());
    }

    // �������� ������ ����� �� ���� �������������
}
