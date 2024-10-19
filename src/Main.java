package tasks;

import controllers.Managers;
import controllers.TaskManager;
import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;
import model.util.Status;
import model.util.TypeTask;

public class Main {
        public static void main(String[] args) {
                TaskManager tm = Managers.getDefault();


                Task task1 = new Task("������ 1", "�������� 1", TypeTask.TASK);
                Task task2 = new Task("������ 2", "�������� 2", TypeTask.TASK);
                tm.setTask(task1);
                tm.setTask(task2);


                Epic epic1 = new Epic("���� 1", "�������� ����� 1");
                tm.setTask(epic1);


                Subtask subtask1 = new Subtask("��������� 1", "�������� ��������� 1", Status.NEW, epic1.getId(), 3);
                Subtask subtask2 = new Subtask("��������� 2", "�������� ��������� 2", Status.NEW, epic1.getId(), 4);
                Subtask subtask3 = new Subtask("��������� 3", "�������� ��������� 3", Status.NEW, epic1.getId(), 5);

                tm.setTask(subtask1);
                tm.setTask(subtask2);
                tm.setTask(subtask3);


                epic1.addSubtask(subtask1);
                epic1.addSubtask(subtask2);

                Epic epic2 = new Epic("���� 2", "�������� ����� 2");
                tm.setTask(epic2); // id 6
                epic2.addSubtask(subtask3);


                System.out.println("��� ������: " + tm.getAllTasks());
                System.out.println("��� ���������: " + tm.getAllSubtasks());
                System.out.println("��� �����: " + tm.getAllEpics());


                tm.updateTask(new Task("����� ��� ������ 1", "����� �������� ������ 1", Status.DONE, task1.getId()));
                tm.updateTask(new Subtask("����� ��� ��������� 1", "����� ��������", Status.IN_PROGRESS, subtask1.getEpicId(), subtask1.getId()));
                tm.updateTask(new Subtask(subtask3.getName(), subtask3.getDescription(), Status.DONE, subtask3.getEpicId(), subtask3.getId()));


                System.out.println("..............");
                System.out.println("�����: " + tm.getAllEpics());
                System.out.println("��������� ����� 1: " + tm.getSubtaskEpic(epic1));


                System.out.println(".............");
                tm.removeTaskById(task1.getId()); // �������� ������ �� id
                tm.removeTaskById(epic2.getId()); // �������� ����� �� id
                System.out.println("��� ������ ����� ��������: " + tm.getAllTasks());
                System.out.println("��� ��������� ����� ��������: " + tm.getAllSubtasks());
                System.out.println("��� ����� ����� ��������: " + tm.getAllEpics());

                // �������� �������
                System.out.println("////////");
                for (int i = 0; i < 15; i++) {
                        tm.getTaskById(subtask1.getId());
                }
                tm.getTaskById(subtask2.getId());
                int i = 1;
                System.out.println("������� �����:");
                for (Task arg : tm.getHistory()) {
                        System.out.println(arg.getId() + " " + i++);
                }
        }
}
