package model.tasks;

import model.util.Status;
import model.util.TypeTask;

public class Subtask extends Task {
    private int epicId; // ID родительского эпика

    // Конструктор для создания подзадачи
    public Subtask(String name, String description, Status status, int id, int epicId) {
        super(name, description, TypeTask.SUBTASK); // Вызов конструктора родительского класса
        this.epicId = epicId;
        this.setStatus(status); // Установка статуса
        this.setId(id); // Установка ID
    }

    // Получение ID эпика
    public int getEpicId() {
        return epicId;
    }

    // Установка ID эпика
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    // Переопределение toString для лучшего представления
    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' + // Получение имени из родительского класса
                ", description='" + getDescription() + '\'' + // Получение описания из родительского класса
                ", status=" + getStatus() + // Получение статуса из родительского класса
                ", id=" + getId() + // Получение ID из родительского класса
                ", epicId=" + epicId +
                '}';
    }
}
