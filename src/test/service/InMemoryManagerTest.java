package test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.ManagerProvider;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryManagerTest {
    @DisplayName("Получение TaskManager по умолчанию")
    @Test
    void getDefaultTest() {
        TaskManager tm = ManagerProvider.getDefault();
        assertNotNull(tm, "TaskManager не должен быть null");
    }

    @DisplayName("Получение HistoryManager по умолчанию")
    @Test
    void getDefaultHistoryManagerTest() {
        HistoryManager hm = ManagerProvider.getDefaultHistory();
        assertNotNull(hm, "HistoryManager не должен быть null");
    }
}