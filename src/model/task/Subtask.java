package model.task;

import model.dictionary.Status;
import model.dictionary.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

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
