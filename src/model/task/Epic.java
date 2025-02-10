package model.task;

import model.dictionary.Status;
import model.dictionary.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW, Duration.ZERO, null, 0);
        this.typeTask = TaskType.EPIC;
    }

    public Epic(String name, String description, int id) {
        super(name, description, Status.NEW, Duration.ZERO, null, id);
        this.typeTask = TaskType.EPIC;
    }

    public List<Integer> getSubtasksId() {
        return new ArrayList<>(subtasksId);
    }

    public void addSubtaskId(int subtaskId) {
        if (!subtasksId.contains(subtaskId)) {
            subtasksId.add(subtaskId);
        }
    }

    public void removeSubtaskId(int subtaskId) {
        subtasksId.remove(Integer.valueOf(subtaskId));
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void recalculateFields(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            this.duration = Duration.ZERO;
            this.startTime = null;
            this.endTime = null;
            return;
        }

        this.duration = subtasks.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        this.startTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        this.endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    // Используйте геттер и сеттер для работы с status
    @Override
    public Status getStatus() {
        return super.getStatus(); // вызываем метод родительского класса
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status); // вызываем метод родительского класса
    }

    @Override
    public String toString() {
        return super.toString() +
                (subtasksId.isEmpty() ? "" : " subtasksId=" + subtasksId) +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
