package model.task;

import model.dictionary.TaskType;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.typeTask = TaskType.EPIC;
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
        if (subtasksId.isEmpty()) {
            return super.toString();
        } else {
            return super.toString() +
                    " id ????????=" + subtasksId +
                    "}";
        }
    }
}
