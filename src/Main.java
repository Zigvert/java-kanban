package tasks;

import service.ManagerProvider;
import service.TaskManager;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import model.dictionary.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        TaskManager tm = ManagerProvider.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ZERO, null, 0);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ZERO, null, 1);
        tm.setTask(task1);
        tm.setTask(task2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 2, 5);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, Duration.ofHours(3), LocalDateTime.of(2024, 12, 16, 12, 30), 3, 5);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2024, 12, 16, 16, 0), 4, 6);

        tm.setTask(subtask1);
        tm.setTask(subtask2);
        tm.setTask(subtask3);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", 5);
        tm.setTask(epic1);
        epic1.addSubtaskId(subtask1.getId());
        epic1.addSubtaskId(subtask2.getId());

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", 6);
        tm.setTask(epic2);
        epic2.addSubtaskId(subtask3.getId());

        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllSubtasks());
        System.out.println(tm.getAllEpics());

        tm.updateTask(new Task("Новое имя Задачи 1", "Новое описание задачи 1", Status.DONE, Duration.ZERO, null, task1.getId()));

        tm.updateTask(new Subtask("Новое имя подзадачи 1", "Новое описание", Status.IN_PROGRESS, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 17, 30), 2, subtask1.getEpicId()));

        System.out.println("..............");
        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllSubtasks());
    }
}
