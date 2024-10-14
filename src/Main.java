public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // �������� �����
        Task task1 = taskManager.createTask("�������", "������� �� ����� ��������");
        Task task2 = taskManager.createTask("������� ����������", "������ ����� ����������");

        // �������� ������ � ��������
        Epic epic1 = taskManager.createEpic("����������� ���������", "����������� ��������� ���������");
        Subtask subtask1 = taskManager.createSubtask("���������� ������", "���������� ���� �������������", epic1.getId());
        Subtask subtask2 = taskManager.createSubtask("������ ���", "������ ��� ��� ���������", epic1.getId());

        Epic epic2 = taskManager.createEpic("������� ��������", "������� ����� ��������");
        Subtask subtask3 = taskManager.createSubtask("������� ���������", "������� ��� ����������� ���������", epic2.getId());

        // ����� ������ �����, �������� � ������
        System.out.println("������:");
        System.out.println(taskManager.getAllTasks());

        System.out.println("���������:");
        System.out.println(taskManager.getAllSubtasks());

        System.out.println("�����:");
        System.out.println(taskManager.getAllEpics());

        // ��������� ��������
        task1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        epic1.updateStatus(); // ��������� ������ ����� �� ������ ��������

        // ����� ��������
        System.out.println("������� ����� ���������:");
        System.out.println("Task1: " + task1.getStatus());
        System.out.println("Subtask1: " + subtask1.getStatus());
        System.out.println("Subtask2: " + subtask2.getStatus());
        System.out.println("Epic1: " + epic1.getStatus());

        // �������� ������ � �����
        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic1.getId());

        // ����� ������� ����� ��������
        System.out.println("������ ����� ����� ��������:");
        System.out.println(taskManager.getAllTasks());

        System.out.println("������ ������ ����� ��������:");
        System.out.println(taskManager.getAllEpics());
    }
}
