import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Epic epic = taskManager.createEpic("����������� �����������", "������������ �������");
        Subtask subtask1 = taskManager.createSubtask("������� �����", "����� ���������� �����", epic.getId());
        Subtask subtask2 = taskManager.createSubtask("���������� ������", "��������� �����������", epic.getId());

        System.out.println("��������� �����:");
        System.out.println(taskManager.getSubtasksByEpicId(epic.getId()));

        subtask1.setStatus(TaskStatus.DONE);
        epic.updateStatus(taskManager);
        System.out.println("������ ����� ����� ����������: " + epic.getStatus());

        taskManager.deleteAllSubtasks();
        System.out.println("��������� ����� ��������: " + taskManager.getAllSubtasks());
    }
}
