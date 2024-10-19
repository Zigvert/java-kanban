package controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {
    @DisplayName("�������� �������������")
    @Test
    void getDefaultTest(){
        TaskManager tm = Managers.getDefault();
        assertNotNull(tm, "TaskManager �� ������");
    }

    @DisplayName("�������� ��������� �������")
    @Test
    void getDefaultHistoryManagerTest() {
        HistoryManager hm = Managers.getDefaultHistory();
        assertNotNull(hm, "HistoryManager �� ������");
    }
}
