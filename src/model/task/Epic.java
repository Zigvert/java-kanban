package model.task;

import model.dictionary.TaskType;
import model.dictionary.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    private List<Subtask> subtasks = new ArrayList<>();

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public Epic(String name, String description) {
        super(name, description);
        this.typeTask = TaskType.EPIC;
    }

    public Epic(String name, String description, int id) {
        super(name, description, Status.NEW, id);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void removeSubtaskId(Integer id) {
        subtasksId.remove(id);
    }

    public void setSubtasks(Subtask subtask) {
        subtask.setEpicId(super.getId());
        subtasksId.add(subtask.getId());
    }

    @Override
    public String toString() {
        return super.toString() +
                (subtasksId.isEmpty() ? "" : " subtasksId=" + subtasksId) +
                "}";
    }
}

