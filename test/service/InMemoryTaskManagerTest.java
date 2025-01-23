package service;


import model.task.Epic;
import model.task.Subtask;
import model.dictionary.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void testAddEpicAndSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.setTask(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2024, 12, 16, 9, 0), 1, epic.getId());
        taskManager.setTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW, Duration.ofHours(2), LocalDateTime.of(2024, 12, 16, 10, 0), 2, epic.getId());
        taskManager.setTask(subtask2);

        assertEquals(2, taskManager.getSubtaskEpic(epic).size());
        assertTrue(taskManager.getSubtaskEpic(epic).contains(subtask1));
        assertTrue(taskManager.getSubtaskEpic(epic).contains(subtask2));

        epic.recalculateFields(taskManager.getSubtaskEpic(epic));

        assertEquals(Duration.ofHours(3), epic.getDuration());
        assertNotNull(epic.getStartTime());
        assertNotNull(epic.getEndTime());
    }
}

