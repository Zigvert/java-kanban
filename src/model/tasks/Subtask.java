package model.tasks;

import model.util.Status;
import model.util.TypeTask;

public class Subtask extends Task {
    private int epicId; // ID ������������� �����

    // ����������� ��� �������� ���������
    public Subtask(String name, String description, Status status, int id, int epicId) {
        super(name, description, TypeTask.SUBTASK); // ����� ������������ ������������� ������
        this.epicId = epicId;
        this.setStatus(status); // ��������� �������
        this.setId(id); // ��������� ID
    }

    // ��������� ID �����
    public int getEpicId() {
        return epicId;
    }

    // ��������� ID �����
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    // ��������������� toString ��� ������� �������������
    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' + // ��������� ����� �� ������������� ������
                ", description='" + getDescription() + '\'' + // ��������� �������� �� ������������� ������
                ", status=" + getStatus() + // ��������� ������� �� ������������� ������
                ", id=" + getId() + // ��������� ID �� ������������� ������
                ", epicId=" + epicId +
                '}';
    }
}
