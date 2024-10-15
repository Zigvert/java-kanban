import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Epic epic = taskManager.createEpic(new Epic("����������� �����������", "������������ �������"));
        Subtask subtask1 = taskManager.createSubtask(new Subtask("������� �����", "����� ���������� �����", epic.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("���������� ������", "��������� �����������", epic.getId()));

        System.out.println("��������� �����:");
        System.out.println(taskManager.getSubtasksByEpicId(epic.getId()));

        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateEpicStatus(epic);
        System.out.println("������ ����� ����� ����������: " + epic.getStatus());

        taskManager.deleteAllSubtasks();
        System.out.println("��������� ����� ��������: " + taskManager.getAllSubtasks());
    }
}
