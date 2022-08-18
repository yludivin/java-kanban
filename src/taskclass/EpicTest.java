package taskclass;

import enumclass.Status;
import manager.impl.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private InMemoryTaskManager inMemoryTaskManager;
    private Epic epicForTest;
    private SubTask subTask1;
    private SubTask subTask2;

    private static final String NAME_TASK = "anyTask";
    private static final String DESCRIPTION_TASK = "anyTask";

    public EpicTest() {
        this.inMemoryTaskManager = new InMemoryTaskManager();
        this.epicForTest = (Epic) inMemoryTaskManager.createNewEpic("Epic1", "TestEpic1");
    }

    @AfterEach
    void cleanSubtask(){
        if(epicForTest.getSubTaskListId().size() > 0){
            for (Integer integer : epicForTest.getSubTaskListId()) {
                inMemoryTaskManager.deleteWithId(integer);
            }
            subTask1 = null;
            subTask2 = null;
        }
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
        createSubtask1();
        createSubtask2();
        assertEquals(expectedStatus, epicForTest.status);
    }

    @Test
    void shouldReturnDONEForEpicAndSubtasksDONE(){
        Status expectedStatus = Status.DONE;
        createSubtask1();
        createSubtask2();
        inMemoryTaskManager.updateTask(subTask1,NAME_TASK,
                DESCRIPTION_TASK, Status.DONE);
        inMemoryTaskManager.updateTask(subTask2,NAME_TASK,
                DESCRIPTION_TASK, Status.DONE);
        assertEquals(expectedStatus, epicForTest.status);
    }

    @Test
    void shouldReturnIN_PROGRESSForEpicAndSubtasksNEWplusDONE(){
        Status expectedStatus = Status.IN_PROGRESS;
        createSubtask1();
        createSubtask2();
        inMemoryTaskManager.updateTask(subTask2,NAME_TASK,
                DESCRIPTION_TASK, Status.DONE);
        assertEquals(expectedStatus, epicForTest.status);
    }

    @Test
    void shouldReturnIN_PROGRESSForEpicAndSubtasksIN_PROGRESS(){
        Status expectedStatus = Status.IN_PROGRESS;
        createSubtask1();
        createSubtask2();
        inMemoryTaskManager.updateTask(subTask1,NAME_TASK,
                DESCRIPTION_TASK, Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(subTask2,NAME_TASK,
                DESCRIPTION_TASK, Status.IN_PROGRESS);
        assertEquals(expectedStatus, epicForTest.status);
    }

    public void createSubtask1(){
        this.subTask1 = (SubTask) this.inMemoryTaskManager.createNewSubtask(NAME_TASK,
                DESCRIPTION_TASK, epicForTest);
    }
    public void createSubtask2(){
        this.subTask2 = (SubTask) this.inMemoryTaskManager.createNewSubtask(NAME_TASK,
                DESCRIPTION_TASK, epicForTest);
    }
}