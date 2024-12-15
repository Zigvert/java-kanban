package model.task;

import model.dictionary.TaskType;
import model.dictionary.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.typeTask = TaskType.EPIC;
    }

    public Epic(String name, String description, int id) {
        super(name, description, Status.NEW, id);
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

    @Override
    public String toString() {
        return super.toString() +
                (subtasksId.isEmpty() ? "" : " subtasksId=" + subtasksId) +
                "}";
    }
}
