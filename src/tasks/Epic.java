package tasks;

import java.util.ArrayList;
import java.util.List;
import manager.TaskManager;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    public Epic(int id, String title, String description) {
        super(id, title, description, TaskStatus.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public List<Integer> getSubtaskIds() { return subtaskIds; }

    public void updateStatus(TaskManager manager) {
        if (subtaskIds.isEmpty()) {
            setStatus(TaskStatus.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = manager.getSubtaskById(subtaskId);
            if (subtask != null) {
                if (subtask.getStatus() != TaskStatus.DONE) allDone = false;
                if (subtask.getStatus() != TaskStatus.NEW) allNew = false;
            }
        }

        if (allDone) setStatus(TaskStatus.DONE);
        else if (allNew) setStatus(TaskStatus.NEW);
        else setStatus(TaskStatus.IN_PROGRESS);
    }
}
