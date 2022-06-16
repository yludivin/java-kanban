package managers.task;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;

public class Task {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
