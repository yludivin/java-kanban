package manager.impl;

import enumclass.TypeTask;
import manager.interfaces.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends InMemoryHistoryManager {

    private static Task task;
    private static final int TASK_ID = 1;
    private static Epic epic;
    private static final int EPIC_ID = 2;
    private static SubTask subTask1;
    private static final int SUBTASK1_ID = 3;
    private static SubTask subTask2;
    private static final int SUBTASK2_ID = 4;

    private static InMemoryHistoryManager historyManager = new InMemoryHistoryManagerTest();
    private static String TEXT = "SomeText";

    @BeforeEach
    void clearHistory(){
        historyManager = new InMemoryHistoryManagerTest();
    }

    @Test
    void shouldReturnTrueForHistoryManagerEmpty(){
        assertTrue(historyManager.getHistory().size() == 0);
    }

    @Test
    void shouldReturnTrueForHistoryManagerAdd1Task(){   //добавляю задачу в пустой список
        createTask();
        historyManager.add(task);
        List<Task> taskList= historyManager.getHistory();
        boolean condition1 = taskList.size() == 1;
        boolean condition2 = taskList.get(0).getTypeTask() == TypeTask.TASK;
        assertTrue(condition1 && condition2);
    }

    @Test
    void shouldReturnTrueForHistoryManagerAdd1Task1Epic2Subtask(){   //добавляю задачи в пустой список
        createTask();
        createEpicAndTwoSubtasks();
        historyManager.add(task);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(epic);
        List<Task> taskList= historyManager.getHistory();
        boolean condition1 = taskList.size() == 4;
        boolean condition2 = taskList.get(0).getTypeTask() == TypeTask.EPIC;
        boolean condition3 = taskList.get(1).getTypeTask() == TypeTask.SUB_TASK;
        boolean condition4 = taskList.get(2).getTypeTask() == TypeTask.SUB_TASK;
        boolean condition5 = taskList.get(3).getTypeTask() == TypeTask.TASK;
        assertTrue(condition1 && condition2 && condition3 && condition4 && condition5);
    }

    @Test
    void shouldReturnTrueForHistoryManagerDeleteLast(){        //удаляю последнюю задачу из просмотра
        createTask();
        createEpicAndTwoSubtasks();
        historyManager.add(task);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(epic);
        historyManager.remove(epic.getId());
        List<Task> taskList = historyManager.getHistory();
        assertTrue(taskList.get(0).getTypeTask() == TypeTask.SUB_TASK);
    }

    @Test
    void shouldReturnTrueForHistoryManagerDeleteFirst(){        //удаляю первую задачу из просмотра
        createTask();
        createEpicAndTwoSubtasks();
        historyManager.add(task);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(epic);
        historyManager.remove(task.getId());
        List<Task> taskList = historyManager.getHistory();
        assertTrue(taskList.get(2).getTypeTask() == TypeTask.SUB_TASK);
    }

    @Test
    void shouldReturnTrueForHistoryManagerDeleteMiddle(){   //удаляю среднюю задачу из просмотра
        createTask();
        createEpicAndTwoSubtasks();
        historyManager.add(task);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(epic);
        historyManager.remove(subTask2.getId());
        List<Task> taskList = historyManager.getHistory();
        assertTrue(taskList.get(0).getTypeTask() == TypeTask.EPIC &&
                taskList.get(1).getTypeTask() == TypeTask.SUB_TASK &&
                taskList.get(2).getTypeTask() == TypeTask.TASK);



    }

    void createEpicAndTwoSubtasks(){
        epic = new Epic(EPIC_ID,TEXT,TEXT);
        subTask1 = new SubTask(SUBTASK1_ID,TEXT,TEXT, epic);
        subTask2 = new SubTask(SUBTASK2_ID,TEXT,TEXT, epic);
    }

    void createTask(){
        task = new Task(TASK_ID,TEXT,TEXT);
    }

    void fillHistory(){

    }
}