package tasks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Task> historyMap = new LinkedHashMap<>();

    @Override
    public void add(Task task) {
        historyMap.put(task.getId(), task);
        if (historyMap.size() > 10) {
            Integer firstKey = historyMap.keySet().iterator().next();
            historyMap.remove(firstKey);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyMap.values());
    }
}
