package test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.ManagerProvider;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryManagerTest {
    @DisplayName("�������� �������������")
    @Test
    void getDefaultTest() {
        TaskManager tm = ManagerProvider.getDefault();
        assertNotNull(tm, "TaskManager �� ������");
    }

    @DisplayName("�������� ��������� �������")
    @Test
    void getDefaultHistoryManagerTest() {
        HistoryManager hm = ManagerProvider.getDefaultHistory();
        assertNotNull(hm, "HistoryManager �� ������");
    }
}
