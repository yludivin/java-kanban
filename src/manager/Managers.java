package manager;

import manager.impl.FileBackedTasksManager;
import manager.impl.HTTPTaskManager;
import manager.impl.InMemoryHistoryManager;
import manager.impl.InMemoryTaskManager;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;

import java.net.URL;
import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getFileBackedManagerByPath(String path) {
        return new FileBackedTasksManager(path);
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HTTPTaskManager getHTTPTaskManagerByPath(URL url) {
        return new HTTPTaskManager(url);
    }

}
