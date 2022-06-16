package managers.history;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;

public class History {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
