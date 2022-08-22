import manager.impl.FileBackedTasksManager;
import manager.interfaces.TaskManager;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private static String pathTestFile = "D:\\yp\\java-kanban\\test\\dataTest\\testData";
    private TaskManager taskManager;


    public FileBackedTasksManagerTest() {
        taskManager = new FileBackedTasksManager(Path.of(pathTestFile));
    }
}
