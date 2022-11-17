/*
package api;

import com.google.gson.Gson;
import manager.impl.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.io.IOException;

public class HttpTaskServerTest {

    HttpTaskServer server;

    FileBackedTasksManager taskManager;

    Gson gson;

    @BeforeEach
    public void init() throws IOException {
        server = new HttpTaskServer();
        taskManager = server.
    }

    @AfterEach
    public void stop(){
        server.stop();
    }


    void createNewEpicAndTwoNewSubtasksInManeger() {
        taskManager.createNewEpic(createTestEpic());
        taskManager.createNewSubtask(createTestSubtask1());
        taskManager.createNewSubtask(createTestSubtask2());
    }

    public Task createTestTask() {
        String startTime = "12:20 30-08-2022";
        long duration = 60;
        task = new Task(NAME_TASK, DESCRIPTION_TASK, startTime, duration);
        return task;
    }

    public SubTask createTestSubtask1() {
        String startTime = "12:20 24-08-2022";
        long duration = 300;
        subTask1 = new SubTask(NAME_TASK, DESCRIPTION_TASK, startTime, duration, epic);
        return subTask1;
    }

    public SubTask createTestSubtask2() {
        String startTime = "17:45 24-08-2022";
        long duration = 400;
        subTask2 = new SubTask(NAME_TASK, DESCRIPTION_TASK, startTime, duration, epic);
        return subTask2;
    }

    public Epic createTestEpic() {
        epic = new Epic(NAME_TASK, DESCRIPTION_TASK);
        return epic;
    }
}
*/
