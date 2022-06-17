package manager.impl;

import manager.interfaces.HistoryManager;
import taskclass.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyBuffer;
    private static int BUFFER_SIZE = 10;

    public InMemoryHistoryManager() {
        this.historyBuffer = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if(task != null) {
            if (historyBuffer.size() < BUFFER_SIZE) {
                historyBuffer.add(task);
            } else {
                historyBuffer.remove(0);
                historyBuffer.add(task);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyBuffer;
    }
}