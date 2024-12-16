package model.task;

import model.dictionary.Status;
import model.dictionary.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtasksId = new ArrayList<>();
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime;
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
        return subtasksId;
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void addSubtaskId(int subtaskId) {
        if (!subtasksId.contains(subtaskId)) {
            subtasksId.add(subtaskId);
        }
    }

    public void removeSubtaskId(int subtaskId) {
        subtasksId.remove(Integer.valueOf(subtaskId));
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }


    public void recalculateFields(List<Subtask> subtasks) {
        duration = subtasks.stream()
                .map(Subtask::getDuration)
                .filter(d -> d != null)
                .reduce(Duration.ZERO, Duration::plus);

        startTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(st -> st != null)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(et -> et != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
