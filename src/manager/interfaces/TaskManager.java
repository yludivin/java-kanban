package manager.interfaces;

import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.util.Map;
import java.util.TreeSet;


public interface TaskManager {

    void getTasks();
    void getTaskById(Integer id);
    void updateTask(Task oldTask, Task newTask);
    void deleteTaskById(int id);

    void deleteAllTasks();

    Task createNewTask(Task task);

    Task createNewSubtask(SubTask subTask);

    Task createNewEpic(Epic epic);


    void allSubtaskFromEpic(Epic epic);

    Integer getTasksQuantity();

    Map<Integer, Task> getTaskMap();

    TreeSet<Task> getPrioritizedTasks();

    public HistoryManager getInMemoryHistoryManager();
}
