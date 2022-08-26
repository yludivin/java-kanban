package manager.interfaces;

import enumclass.Status;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeSet;


public interface TaskManager {

    void getAllTasksName();

    void deleteAll();

    Task createNewTask(Task task);

    Task createNewSubtask(SubTask subTask);

    Task createNewEpic(Epic epic);

    void getTask(Integer id);

    void updateTask(Task oldTask, Task newTask);
    void deleteWithId(int id);

    void allSubtaskFromEpic(Epic epic);

    Integer getTasksQuantity();

    Map<Integer, Task> getTaskMap();

    TreeSet<Task> getPrioritizedTasks();
}
