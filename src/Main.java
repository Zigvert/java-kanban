import tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        // �������� �����
        Epic epic = taskManager.createEpic(new Epic("����������� �����������", "������������ �������"));
        // �������� ��������
        Subtask subtask1 = taskManager.createSubtask(new Subtask("������� �����", "����� ���������� �����", epic.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("���������� ������", "��������� �����������", epic.getId()));

        System.out.println("��������� �����:");
        System.out.println(taskManager.getSubtasksByEpicId(epic.getId()));

        // ���������� ������� ���������
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);

        // ���������� ������� �����
        System.out.println("������ ����� ����� ����������: " + epic.getStatus());

        // �������� ���������
        taskManager.deleteSubtaskById(subtask2.getId());
        System.out.println("��������� ����� �������� �����: " + taskManager.getSubtasksByEpicId(epic.getId()));

        // ������ ������� ����������
        System.out.println("������� ����������:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }
}
