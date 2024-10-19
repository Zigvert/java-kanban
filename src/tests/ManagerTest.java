package controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {
    @DisplayName("Создание таскМенеджера")
    @Test
    void getDefaultTest(){
        TaskManager tm = Managers.getDefault();
        assertNotNull(tm, "TaskManager не создан");
    }

    @DisplayName("Создание менеджера истории")
    @Test
    void getDefaultHistoryManagerTest() {
        HistoryManager hm = Managers.getDefaultHistory();
        assertNotNull(hm, "HistoryManager не создан");
    }
}
