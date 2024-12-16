package service;

import model.task.Task;
import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1 == null || task2 == null) {
            throw new IllegalArgumentException("Tasks cannot be null");
        }

        if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return 0;
        }

        if (task1.getStartTime() == null) {
            return 1;
        }

        if (task2.getStartTime() == null) {
            return -1;
        }

        return task1.getStartTime().compareTo(task2.getStartTime());
    }
}
