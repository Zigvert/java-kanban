package model.task;

import model.dictionary.Status;
import model.dictionary.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description) {
        super(name, description, Status.NEW, Duration.ZERO, null, 0);
        this.typeTask = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, Status status, Duration duration, LocalDateTime startTime, int id, int epicId) {
        super(name, description, status, duration, startTime, id);
        this.epicId = epicId;
        this.typeTask = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    // Проверка на пересечение времени с другими подзадачами
    public boolean isOverlapping(List<Subtask> subtasks) {
        for (Subtask subtask : subtasks) {
            if (this.getStartTime().isBefore(subtask.getEndTime()) && this.getEndTime().isAfter(subtask.getStartTime())) {
                return true; // Перекрытие с другой подзадачей
            }
        }
        return false;
    }

    // Проверка завершенности подзадачи и обновление статуса эпика
    public boolean isCompleted() {
        return getStatus() == Status.DONE;
    }

    // Обновление статуса эпика в зависимости от статуса подзадач
    public boolean isEpicCompleted(List<Subtask> subtasks) {
        for (Subtask subtask : subtasks) {
            if (!subtask.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", epicId=" + epicId +
                '}';
    }
}
