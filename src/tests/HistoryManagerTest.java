package controllers;

import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class HistoryManagerTest {
    HistoryManager hm = Managers.getDefaultHistory();
    static Epic epic1, epic2;
    static Task task1, task2, task3;
    static Subtask subtask1, subtask2, subtask3, subtask4;

    @BeforeAll
    static void createTask() {
        epic1 = new Epic("Epic 1", "Epic1Description");
        epic2 = new Epic("Epic 2", "Epic2Description");
        task1 = new Task("Task 1", "Task1Description");
        task2 = new Task("Task 2", "Task2Description");
        task3 = new Task("Task 3", "Task3Description");
        subtask1 = new Subtask("SubTask 1", "Subtask1Description");
        subtask2 = new Subtask("SubTask 2", "Subtask2Description");
        subtask3 = new Subtask("SubTask 3", "Subtask3Description");
        subtask4 = new Subtask("SubTask 4", "Subtask4Description");
    }

    @DisplayName("�������� �� ���������� ������")
    @Test
    void addTaskTest() {
        assertEquals(0, hm.getHistoryTask().size(), "��� �������� ������ ������������� ����� �� ����");
        hm.add(task1);
        assertEquals(1, hm.getHistoryTask().size(), "������ �� �����������");
        assertEquals(task1, hm.getHistoryTask().getFirst(), "������ �� ���������");
        hm.add(epic1);
        hm.add(subtask1);
        assertEquals(3, hm.getHistoryTask().size(), "������ ������ ����� �� �����������");
        assertEquals(subtask1, hm.getHistoryTask().getLast(), "��������� ������ �� ���������");

        List<Task> historyTest = Arrays.asList(task1, epic1, subtask1);
        assertArrayEquals(historyTest.toArray(), hm.getHistoryTask().toArray(), "����������� ������ �� ���������");
    }

    @DisplayName("�������� �� ������������ ���������� �����")
    @Test
    void addMaxCapacityTest() {
        for (int i = 0; i < 10; i++) {
            hm.add(new Task("������ " + i, "��������" + i));
        }
        assertEquals(10, hm.getHistoryTask().size(), "������������ ���������� ����� �� ���������");
        hm.add(epic2);
        assertEquals(epic2, hm.getHistoryTask().getLast(), "��������� ������� �� ���������");
    }
}