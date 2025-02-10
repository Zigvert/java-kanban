package model.task;

import model.dictionary.Status;
import model.dictionary.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final String name;
    private final String description;
    private int id;
    private Status status;
    protected TaskType typeTask;
    protected Duration duration;
    protected LocalDateTime startTime;

    // Конструктор для создания задачи с полным набором параметров
    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.typeTask = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
        this.id = id;
    }

    // Конструктор для задач без идентификатора и времени начала
    public Task(String name, String description, Status status) {
        this(name, description, status, Duration.ZERO, null, 0);
    }

    // Геттеры и сеттеры
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getTypeTask() {
        return typeTask;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    // Время окончания задачи вычисляется на основе времени начала и продолжительности
    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    // Переопределение equals для проверки задач по ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    // Переопределение hashCode для корректной работы в коллекциях
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Переопределение метода toString для красивого вывода информации о задаче
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
