package service;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private File file;
    private FileBackedTaskManager manager;

    @BeforeEach
    public void setUp() throws IOException {

        file = new File("test_tasks.csv");
        if (file.exists()) {
            file.delete();
        }

        manager = new FileBackedTaskManager(file);

        Task task1 = new Task("Task 1", "Description 1", Status.NEW, 0);
        Task task2 = new Task("Task 2", "Description 2", Status.IN_PROGRESS, 1);
        Epic epic = new Epic("Epic 1", "Epic Description");

        manager.setTask(task1);
        manager.setTask(task2);
        manager.setTask(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", Status.NEW, 2, epic.getId());
        manager.setTask(subtask);
    }

    @Test
    public void testSaveAndLoadTasks() {

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        assertTrue(compareTasksIgnoringId(new ArrayList<>(manager.getTasks().values()),
                new ArrayList<>(loadedManager.getTasks().values())));

        assertTrue(compareTasksIgnoringId(new ArrayList<>(manager.getEpics().values()),
                new ArrayList<>(loadedManager.getEpics().values())));

        assertTrue(compareTasksIgnoringId(new ArrayList<>(manager.getSubtasks().values()),
                new ArrayList<>(loadedManager.getSubtasks().values())));
    }

    private boolean compareTasksIgnoringId(List<Task> tasks1, List<Task> tasks2) {

        if (tasks1.size() != tasks2.size()) {
            return false;
        }

        for (int i = 0; i < tasks1.size(); i++) {
            Task task1 = tasks1.get(i);
            Task task2 = tasks2.get(i);

            if (!task1.getName().equals(task2.getName()) ||
                    !task1.getDescription().equals(task2.getDescription()) ||
                    !task1.getStatus().equals(task2.getStatus()) ||
                    !task1.getTypeTask().equals(task2.getTypeTask())) {
                return false;
            }
        }
        return true;
    }
}
