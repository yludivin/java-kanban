import enumclass.Status;
import enumclass.TypeTask;
import manager.impl.InMemoryHistoryManager;
import manager.impl.InMemoryTaskManager;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest{
    protected static Task task;
    protected static Epic epic;
    protected static SubTask subTask1;
    protected static SubTask subTask2;
    protected static final String TEXT = "SomeText";

    protected static TaskManager taskManager;

    public TaskManagerTest(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    void cleanTasks(){
        taskManager.deleteAll();
    }

    @Test
    void shouldReturn3ForTaskManagerReturnTasksQuantity(){
        int expectedNumber = 3;
        taskManager.createNewTask(TEXT,TEXT);
        epic = (Epic) taskManager.createNewEpic(TEXT,TEXT);
        taskManager.createNewSubtask(TEXT, TEXT, epic);
        assertEquals(expectedNumber,taskManager.getTasksQuantity());
    }

    @Test
    void shouldReturn4ForTaskManagerGetAllTasksName(){
        int expectedNumber = 4;
        taskManager.createNewTask(TEXT,TEXT);
        epic = (Epic) taskManager.createNewEpic(TEXT,TEXT);
        taskManager.createNewSubtask(TEXT, TEXT, epic);
        taskManager.createNewSubtask(TEXT, TEXT, epic);
        assertEquals(expectedNumber,taskManager.getTasksQuantity());
    }

    @Test
    void shouldReturn0ForTaskManagerDelleteAll(){
        int expectedNumber = 0;
        taskManager.createNewTask(TEXT,TEXT);
        epic = (Epic) taskManager.createNewEpic(TEXT,TEXT);
        taskManager.createNewSubtask(TEXT, TEXT, epic);
        taskManager.deleteAll();
        assertEquals(expectedNumber,taskManager.getTasksQuantity());
    }

    @Test
    void shouldReturnTrueForTaskManagerCreateNewTask(){
        int expectedNumberTasks = 1;
        task = taskManager.createNewTask(TEXT, TEXT);
        assertTrue(taskManager.getTasksQuantity() == expectedNumberTasks &&
                (taskManager.getTaskMap()).get(task.getId()).getTypeTask().equals(TypeTask.TASK));
    }

    @Test
    void shouldReturnTrueForTaskManagerCreateNewEpic(){
        int expectedNumberEpic = 1;
        epic = (Epic) taskManager.createNewEpic(TEXT, TEXT);
        assertTrue(taskManager.getTasksQuantity() == expectedNumberEpic &&
                (taskManager.getTaskMap()).get(epic.getId()).getTypeTask().equals(TypeTask.EPIC));
    }

    @Test
    void shouldReturnTrueForTaskManagerCreateNewSubtaskAndEpic(){
        createNewEpicAndTwoNewSubtasks();
        boolean condition1 = taskManager.getTasksQuantity() == 3;
        boolean condition2 = epic.getSubTaskListId().size() == 2;
        boolean condition3 = true;
        if(subTask1.getId() != epic.getSubTaskListId().get(0) ||
                subTask2.getId() != epic.getSubTaskListId().get(1)){
            condition3 = false;
        }
        assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    void shouldReturnTrueForEpicStatusCreateNewEpicSTATUS_NEW(){
        Status expected = Status.NEW;
        createNewEpicAndTwoNewSubtasks();
        assertTrue(epic.getStatus() == expected && subTask1.getStatus() == expected &&
                subTask2.getStatus() == expected);
    }

    @Test
    void shouldReturnTrueForEpicStatusCreateNewEpicSTATUS_IN_PROGRESS(){
        Status expected = Status.IN_PROGRESS;
        createNewEpicAndTwoNewSubtasks();
        taskManager.updateTask(subTask1,TEXT, TEXT, Status.IN_PROGRESS);
        assertTrue(epic.getStatus() == expected && subTask1.getStatus() == expected);
    }

    @Test
    void shouldReturnTrueForEpicStatusCreateNewEpicSTATUS_DONE(){
        Status expected = Status.DONE;
        createNewEpicAndTwoNewSubtasks();
        taskManager.updateTask(subTask1,TEXT, TEXT, Status.DONE);
        taskManager.updateTask(subTask2,TEXT, TEXT, Status.DONE);
        assertTrue(epic.getStatus() == expected && subTask1.getStatus() == expected &&
                subTask2.getStatus() == expected);
    }

    @Test
    void shouldReturnFalseForUpdateTaskForTask(){
        task = taskManager.createNewTask(TEXT, TEXT);
        Task tempTask =  new Task(task.getId(), task.getName(), task.getDescription());
        taskManager.updateTask(task, TEXT, TEXT, Status.IN_PROGRESS);
        assertFalse(compareTasks(task,tempTask));
    }

    @Test
    void shouldReturnFalseForUpdateTaskEpic(){
        epic = (Epic)taskManager.createNewEpic(TEXT,TEXT);
        Task tempEpic =  new Epic(epic.getId(), epic.getName(), epic.getDescription());
        taskManager.updateTask(epic, TEXT, TEXT, Status.IN_PROGRESS);
        assertFalse(compareTasks(epic,tempEpic));
    }

    @Test
    void shouldReturnFalseForUpdateTaskSubtask(){
        epic = (Epic)taskManager.createNewEpic(TEXT,TEXT);
        subTask1 = (SubTask) taskManager.createNewSubtask(TEXT, TEXT, epic);
        Task tempSubtask =  new SubTask(subTask1.getId(), subTask1.getName(), subTask1.getDescription());
        taskManager.updateTask(subTask1, TEXT, TEXT, Status.IN_PROGRESS);
        assertFalse(compareTasks(subTask1,tempSubtask));
    }

    @Test
    void shouldReturnNullForDeleteWithId(){
        createNewEpicAndTwoNewSubtasks();
        int idSubtask = subTask1.getId();
        taskManager.deleteWithId(idSubtask);
        assertNull(taskManager.getTaskMap().get(idSubtask));
    }

    @Test
    abstract void shouldReturnTrueForGetTask();

    void createNewEpicAndTwoNewSubtasks(){
        epic = (Epic) taskManager.createNewEpic(TEXT, TEXT);
        subTask1 = (SubTask) taskManager.createNewSubtask(TEXT, TEXT, epic);
        subTask2 = (SubTask) taskManager.createNewSubtask(TEXT, TEXT, epic);
    }

    <T extends Task> boolean compareTasks(T task1, T task2){
        return task1.getId() == task2.getId() &&
                task1.getName().equals(task2.getName()) &&
                task1.getDescription().equals(task2.getDescription()) &&
                 task1.getStatus().equals(task2.getStatus());

    }
}

/*

Для двух менеджеров задач InMemoryTasksManager и FileBackedTasksManager.
Чтобы избежать дублирования кода, необходим базовый класс с тестами на каждый
метод из интерфейса abstract class TaskManagerTest<T extends TaskManager>.
-Для подзадач нужно дополнительно проверить наличие эпика, а для эпика — расчёт статуса.
-Для каждого метода нужно проверить его работу:
a. Со стандартным поведением.
b. С пустым списком задач.
c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).

public interface TaskManager {

    void getAllTasksName(); +/-

    void deleteAll();   +

    Task createNewTask(String name, String description);    +

    Task createNewSubtask(String name, String description, Epic epic);

    Task createNewEpic(String name, String description);    +

    void getTask(Integer id);

    void updateTask(Task abstractTask, String name, String description, Status status); +

    void deleteWithId(int id);+

    void allSubtaskFromEpic(Epic epic);

    Integer returnTasksQuantity(); +
}*/
