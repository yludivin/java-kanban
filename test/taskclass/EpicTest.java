package taskclass;

import enumclass.Status;
import manager.impl.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private InMemoryTaskManager inMemoryTaskManager;
    private Epic epicForTest;
    private SubTask subTask1;
    private SubTask subTask2;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    private static final String NAME_TASK = "anyTask";
    private static final String DESCRIPTION_TASK = "anyTask";

    public EpicTest() {
        this.inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic(NAME_TASK, DESCRIPTION_TASK);
        this.epicForTest = (Epic) inMemoryTaskManager.createNewEpic(epic);
    }

    @AfterEach
    void cleanSubtask() {
        epicForTest = null;
        subTask1 = null;
        subTask2 = null;
    }


    @Test
    void shouldReturn0ForEpicAndSubtasks0() {
        int expectedLength = 0;
        List<Integer> subtasks = epicForTest.getSubTaskListId();
        assertEquals(expectedLength, subtasks.size(),
                "ожидалась длина списка - " + expectedLength + " .По факту длина - " + subtasks.size());
    }

    @Test
    void shouldReturnNEWForEpicAndSubtasksNEW() {
        Status expectedStatus = Status.NEW;
        createSubtask1();
        createSubtask2();
        assertEquals(expectedStatus, epicForTest.getStatus());
    }

    @Test
    void shouldReturnDONEForEpicAndSubtasksDONE() {
        Status expectedStatus = Status.DONE;
        createSubtask1();
        createSubtask2();
        SubTask updatedTask1 = new SubTask(NAME_TASK, DESCRIPTION_TASK,
                "12:20 24-08-2022", 350, epicForTest);
        updatedTask1.setStatus(Status.DONE);
        SubTask updatedTask2 = new SubTask(NAME_TASK, DESCRIPTION_TASK,
                "19:45 24-08-2022", 350, epicForTest);
        updatedTask2.setStatus(Status.DONE);
        inMemoryTaskManager.updateTask(subTask1, updatedTask1);
        inMemoryTaskManager.updateTask(subTask2, updatedTask2);
        assertEquals(expectedStatus, epicForTest.getStatus());
    }

    @Test
    void shouldReturnIN_PROGRESSForEpicAndSubtasksNEWplusDONE() {
        Status expectedStatus = Status.IN_PROGRESS;
        createSubtask1();
        createSubtask2();
        SubTask updatedTask2 = new SubTask(NAME_TASK, DESCRIPTION_TASK,
                "17:45 24-08-2022", 350, epicForTest);
        updatedTask2.setStatus(expectedStatus);
        inMemoryTaskManager.updateTask(subTask2, updatedTask2);
        assertEquals(expectedStatus, epicForTest.getStatus());
    }

    @Test
    void shouldReturnIN_PROGRESSForEpicAndSubtasksIN_PROGRESS() {
        Status expectedStatus = Status.IN_PROGRESS;
        createSubtask1();
        createSubtask2();
        SubTask updatedTask1 = new SubTask(NAME_TASK, DESCRIPTION_TASK,
                "12:20 24-08-2022", 350, epicForTest);
        updatedTask1.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(subTask1, updatedTask1);
        SubTask updatedTask2 = new SubTask(NAME_TASK, DESCRIPTION_TASK,
                "19:45 24-08-2022", 350, epicForTest);
        updatedTask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(subTask2, updatedTask2);
        assertEquals(expectedStatus, epicForTest.getStatus());
    }

    public void createSubtask1() {
        String startTime = "12:20 24-08-2022";
        long duration = 300;
        SubTask subTask = new SubTask(NAME_TASK, DESCRIPTION_TASK, startTime, duration, epicForTest);
        this.subTask1 = (SubTask) this.inMemoryTaskManager.createNewSubtask(subTask);
    }

    public void createSubtask2() {
        String startTime = "19:45 24-08-2022";
        long duration = 400;
        SubTask subTask = new SubTask(NAME_TASK, DESCRIPTION_TASK, startTime, duration, epicForTest);
        this.subTask2 = (SubTask) this.inMemoryTaskManager.createNewSubtask(subTask);
    }
}

