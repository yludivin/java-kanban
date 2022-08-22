package manager.interfaces;

import enumclass.Status;
import taskclass.Epic;
import taskclass.Task;

import java.util.Map;


public interface TaskManager {

    void getAllTasksName();

    void deleteAll();

    Task createNewTask(String name, String description);

    Task createNewSubtask(String name, String description, Epic epic);

    Task createNewEpic(String name, String description);

    void getTask(Integer id);

    void updateTask(Task abstractTask, String name, String description, Status status);

    void deleteWithId(int id);

    void allSubtaskFromEpic(Epic epic);

    Integer getTasksQuantity();

    Map<Integer, Task> getTaskMap();
}
