package model.task;

import model.dictionary.Status;
import model.dictionary.TaskType;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description) {
        super(name, description);
        this.typeTask = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
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
        return super.toString() +
                " epicId=" + epicId +
                "}";
    }
}