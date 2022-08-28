package manager.impl;

import enumclass.TypeTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.time.format.DateTimeFormatter;
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
    protected static final String NAME_TASK = "anyName";
    protected static final String DESCRIPTION_TASK = "anyDescription";
    protected InMemoryTaskManager taskManager;                  // При работе с эпиками подзадачи обращаются к
                                                                //  EpicId. Без него уже не получится.
    private static InMemoryHistoryManager historyManager;
    private static String TEXT = "SomeText";
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    @BeforeEach
    void clearHistory(){
        historyManager = new InMemoryHistoryManagerTest();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldReturnTrueForHistoryManagerEmpty(){
        assertTrue(historyManager.getHistory().size() == 0);
    }

    @Test
    void shouldReturnTrueForHistoryManagerAdd1Task(){   //добавляю задачу в пустой список
        taskManager.createNewTask(createTestTask());
        historyManager.add(task);
        List<Task> taskList= historyManager.getHistory();
        boolean condition1 = taskList.size() == 1;
        boolean condition2 = taskList.get(0).getTypeTask() == TypeTask.TASK;
        assertTrue(condition1 && condition2);
    }

    @Test
    void shouldReturnTrueForHistoryManagerAdd1Task1Epic2Subtask(){   //добавляю задачи в пустой список
        taskManager.createNewTask(createTestTask());
        createNewEpicAndTwoNewSubtasksInManeger();
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
    void shouldReturnTrueForHistoryManagerDeleteLast(){        //удаляю эпик и остается только таск
        taskManager.createNewTask(createTestTask());
        createNewEpicAndTwoNewSubtasksInManeger();
        historyManager.add(task);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(epic);
        historyManager.remove(epic.getId());
        List<Task> taskList = historyManager.getHistory();
        assertTrue(taskList.get(0).getTypeTask() == TypeTask.TASK);
    }

    @Test
    void shouldReturnTrueForHistoryManagerDeleteFirst(){        //удаляю первую задачу из просмотра
        taskManager.createNewTask(createTestTask());
        createNewEpicAndTwoNewSubtasksInManeger();
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
        taskManager.createNewTask(createTestTask());
        createNewEpicAndTwoNewSubtasksInManeger();
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

    public Task createTestTask(){
        String startTime = "12:20 30-08-2022";
        long duration = 60;
        task = new Task(NAME_TASK, DESCRIPTION_TASK, startTime,duration);
        return task;
    }

    public SubTask createTestSubtask1(){
        String startTime = "12:20 24-08-2022";
        long duration = 300;
        subTask1 = new SubTask(NAME_TASK, DESCRIPTION_TASK,startTime,duration, epic);
        return subTask1;
    }
    public SubTask createTestSubtask2(){
        String startTime = "17:45 24-08-2022";
        long duration = 400;
        subTask2 = new SubTask(NAME_TASK, DESCRIPTION_TASK,startTime,duration, epic);
        return subTask2;
    }

    public Epic createTestEpic(){
        epic = new Epic(NAME_TASK, DESCRIPTION_TASK);
        return epic;
    }

    void createNewEpicAndTwoNewSubtasksInManeger(){
        taskManager.createNewEpic(createTestEpic());
        taskManager.createNewSubtask(createTestSubtask1());
        taskManager.createNewSubtask(createTestSubtask2());
    }
}
