package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    public Epic(String title, String description) {
        super(title, description);
        this.subtaskIds = new ArrayList<>();
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {  // Исправлено: принимаем сразу Integer
        subtaskIds.remove(subtaskId);
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);  // Защита от изменений извне
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }
}
