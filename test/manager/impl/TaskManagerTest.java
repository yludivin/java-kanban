package manager.impl;

import enumclass.Status;
import enumclass.TypeTask;
import manager.interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest{
    protected static Task task;
    protected static Epic epic;
    protected static SubTask subTask1;
    protected static SubTask subTask2;
    protected static final String NAME_TASK = "anyName";
    protected static final String DESCRIPTION_TASK = "anyDescription";

    protected static TaskManager taskManager;
    protected static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

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
        createTestSubtask1();
        taskManager.createNewTask(createTestTask());
        taskManager.createNewEpic(createTestEpic());
        taskManager.createNewSubtask(createTestSubtask1());
        assertEquals(expectedNumber,taskManager.getTasksQuantity());
    }

    @Test
    void shouldReturn4ForTaskManagerGetAllTasksName(){
        int expectedNumber = 4;
        taskManager.createNewTask(createTestTask());
        taskManager.createNewEpic(createTestEpic());
        taskManager.createNewSubtask(createTestSubtask1());
        taskManager.createNewSubtask(createTestSubtask2());
        assertEquals(expectedNumber,taskManager.getTasksQuantity());
    }

    @Test
    void shouldReturn0ForTaskManagerDelleteAll(){
        int expectedNumber = 0;
        taskManager.createNewTask(createTestTask());
        taskManager.createNewEpic(createTestEpic());
        taskManager.createNewSubtask(createTestSubtask1());
        taskManager.deleteAll();
        assertEquals(expectedNumber,taskManager.getTasksQuantity());
    }

    @Test
    void shouldReturnTrueForTaskManagerCreateNewTask(){
        int expectedNumberTasks = 1;
        taskManager.createNewTask(createTestTask());
        assertTrue(taskManager.getTasksQuantity() == expectedNumberTasks &&
                (taskManager.getTaskMap()).get(task.getId()).getTypeTask().equals(TypeTask.TASK));
    }

    @Test
    void shouldReturnTrueForTaskManagerCreateNewEpic(){
        int expectedNumberEpic = 1;
        taskManager.createNewEpic(createTestEpic());
        assertTrue(taskManager.getTasksQuantity() == expectedNumberEpic &&
                (taskManager.getTaskMap()).get(epic.getId()).getTypeTask().equals(TypeTask.EPIC));
    }

    @Test
    void shouldReturnTrueForTaskManagerCreateNewSubtaskAndEpic(){
        createNewEpicAndTwoNewSubtasksInManeger();
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
        createNewEpicAndTwoNewSubtasksInManeger();
        assertTrue(epic.getStatus() == expected && subTask1.getStatus() == expected &&
                subTask2.getStatus() == expected);
    }

    @Test
    void shouldReturnTrueForEpicStatusCreateNewEpicSTATUS_IN_PROGRESS(){
        Status expected = Status.IN_PROGRESS;
        createNewEpicAndTwoNewSubtasksInManeger();

        SubTask updatedSubtask = new SubTask(subTask1.getName(), subTask1.getDescription(),
                subTask1.getStartTime().format(dateTimeFormatter),
                subTask1.getDuration().toMinutes(), epic);
        updatedSubtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subTask1,updatedSubtask);
        assertTrue(epic.getStatus() == expected && subTask1.getStatus() == expected);
    }

    @Test
    void shouldReturnTrueForEpicStatusCreateNewEpicSTATUS_DONE(){
        Status expected = Status.DONE;
        createNewEpicAndTwoNewSubtasksInManeger();
        SubTask updatedSubtask1 = new SubTask(subTask1.getName(), subTask1.getDescription(),
                subTask1.getStartTime().format(dateTimeFormatter),
                subTask1.getDuration().toMinutes(), epic);
        updatedSubtask1.setStatus(Status.DONE);
        SubTask updatedSubtask2 = new SubTask(subTask1.getName(), subTask1.getDescription(),
                subTask1.getStartTime().format(dateTimeFormatter),
                subTask1.getDuration().toMinutes(), epic);
        updatedSubtask2.setStatus(Status.DONE);
        taskManager.updateTask(subTask1, updatedSubtask1);
        taskManager.updateTask(subTask2, updatedSubtask1);
        assertTrue(epic.getStatus() == expected && subTask1.getStatus() == expected &&
                subTask2.getStatus() == expected);
    }

    @Test
    void shouldReturnFalseForUpdateTaskForTask(){
        taskManager.createNewTask(createTestTask());
        Status taskStatus = task.getStatus();
        Task updatedTask =  new Task(task.getName(), task.getDescription(),
                task.getStartTime().format(dateTimeFormatter), task.getDuration().toMinutes());
        updatedTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task, updatedTask);
        assertFalse(taskStatus == task.getStatus());
    }

    @Test
    void shouldReturnFalseForUpdateTaskEpic(){
        createNewEpicAndTwoNewSubtasksInManeger();
        Status epicOldStatus = epic.getStatus();
        SubTask updatedSubtask1 = new SubTask(subTask1.getName(), subTask1.getDescription(),
                subTask1.getStartTime().format(dateTimeFormatter), subTask1.getDuration().toMinutes(), epic);
        updatedSubtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subTask1, updatedSubtask1);
        assertFalse(epic.getStatus() == epicOldStatus);   //меняется статус с нью на инпрогресс
    }


    @Test
    void shouldReturnNullForDeleteWithId(){
        createNewEpicAndTwoNewSubtasksInManeger();
        int idSubtask = subTask1.getId();
        taskManager.deleteWithId(idSubtask);
        assertNull(taskManager.getTaskMap().get(idSubtask));
    }

    @Test
    abstract void shouldReturnTrueForGetTask();
    //DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    void createNewEpicAndTwoNewSubtasksInManeger(){
        taskManager.createNewEpic(createTestEpic());
        taskManager.createNewSubtask(createTestSubtask1());
        taskManager.createNewSubtask(createTestSubtask2());
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

    <T extends Task> boolean compareTasks(T task1, T task2){
        return task1.getId() == task2.getId() &&
                task1.getName().equals(task2.getName()) &&
                task1.getDescription().equals(task2.getDescription()) &&
                 task1.getStatus().equals(task2.getStatus());

    }
}

