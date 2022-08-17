package taskclass;

import enumclass.Status;
import manager.impl.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static InMemoryTaskManager inMemoryTaskManager;
    private static Epic epicForTest;
    private static SubTask subTask1;
    private static SubTask subTask2;


    @BeforeAll
    static void init(){
        inMemoryTaskManager = new InMemoryTaskManager();
        epicForTest = (Epic) inMemoryTaskManager.createNewEpic("Epic1", "TestEpic1");
    }

    @Test
    void shouldReturn0ForEpicAndSubtasks0(){
        int expectedLength = 0;
        List<Integer> subtasks = epicForTest.getSubTaskListId();
        assertEquals(expectedLength, subtasks.size(),
                "ожидалась длина списка - " + expectedLength + " .По факту длина - " + subtasks.size());
    }

    @Test
    void shouldReturnNEWForEpicAndSubtasksNEW(){
        Status expectedStatus = Status.NEW;
        subTask1 = (SubTask) inMemoryTaskManager.createNewSubtask("subTask1",
                "subTask1", epicForTest);
        subTask2 = (SubTask) inMemoryTaskManager.createNewSubtask("subTask2",
                "subTask2", epicForTest);
        epicForTest.addNewSubTaskId(subTask1.getId());
        epicForTest.addNewSubTaskId(subTask2.getId());
        assertEquals(expectedStatus, epicForTest.status);
    }

    @Test
    void shouldReturnDONEForEpicAndSubtasksDONE(){
        Status expectedStatus = Status.DONE;
        inMemoryTaskManager.updateTask(subTask1,"subTask1",
                "subTask1", Status.DONE);
        inMemoryTaskManager.updateTask(subTask2,"subTask2",
                "subTask2", Status.DONE);
        assertEquals(expectedStatus, epicForTest.status);
    }

    @Test
    void shouldReturnIN_PROGRESSForEpicAndSubtasksNEW_DONE(){
        Status expectedStatus = Status.IN_PROGRESS;

        inMemoryTaskManager.updateTask(subTask1,"subTask1",
                "subTask1", Status.NEW);
        inMemoryTaskManager.updateTask(subTask2,"subTask2",
                "subTask2", Status.DONE);
        assertEquals(expectedStatus, epicForTest.status);
    }


}


/*
    Для расчёта статуса Epic. Граничные условия:
        a. Пустой список подзадач.
        b. Все подзадачи со статусом NEW.
        c. Все подзадачи со статусом DONE.
        d. Подзадачи со статусами NEW и DONE.
        e. Подзадачи со статусом IN_PROGRESS.
        shouldReturn200ForBikeAndDistanceIs20Km
        shouldThrowExceptionForBikeAndDistanceIs21Km

        */
