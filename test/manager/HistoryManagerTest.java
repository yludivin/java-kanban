package manager;

import manager.interfaces.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.util.List;

class HistoryManagerTest {

    private static Task task;
    private static final int TASK_ID = 1;
    private static Epic epic;
    private static final int EPIC_ID = 2;
    private static SubTask subTask1;
    private static final int SUBTASK1_ID = 3;
    private static SubTask subTask2;
    private static final int SUBTASK2_ID = 4;

    private static HistoryManager historyManager;
    private static String TEXT = "SomeText";

    public HistoryManagerTest(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @BeforeEach
    void clearHistory(){
        historyManager = null;
        task = null;
        epic = null;
        subTask1 = null;
        subTask2 = null;
    }

    @Test
    void shouldReturnTrueForHistoryManagerAdd1Task(){   //добавляю задачу в пустой список
        createTask();
        historyManager.add(task);
        //проверяю что всего 1 задача
    }

    @Test
    void shouldReturnTrueForHistoryManagerAdd1Task1Epic2Subtask(){   //добавляю задачу в пустой список
        createTask();
        createEpicAndTwoSubtasks();
        historyManager.add(task);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(epic);
        List<Task> history = List.of(epic, subTask2, subTask1, task);
        //проверяю что всего 4 задачи
    }

    void createEpicAndTwoSubtasks(){
        epic = new Epic(EPIC_ID,TEXT,TEXT);
        subTask1 = new SubTask(SUBTASK1_ID,TEXT,TEXT, epic);
        subTask2 = new SubTask(SUBTASK2_ID,TEXT,TEXT, epic);
    }

    void createTask(){
        Task task = new Task(TASK_ID,TEXT,TEXT);
    }
}