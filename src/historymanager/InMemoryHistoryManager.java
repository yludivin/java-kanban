package historymanager;

import taskclass.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyBuffer;


    public InMemoryHistoryManager() {
        this.historyBuffer = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (historyBuffer.size() < 10) {
            historyBuffer.add(task);
        } else {
            historyBuffer.remove(0);
            historyBuffer.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyBuffer;
    }
}
