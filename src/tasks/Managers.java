package tasks;

public class Managers {
    public static TaskManager getDefault() {
        return new TaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
