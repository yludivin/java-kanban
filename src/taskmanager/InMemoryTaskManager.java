package taskmanager;

import enumclass.Status;
import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import managers.Managers;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasksMap;
    private Integer taskId;
    private HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.taskId = 0;
        this.inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public void getAllTasksName() {
        for (Map.Entry<Integer, Task> task : tasksMap.entrySet()) {
            System.out.println(task.toString());
        }
    }

    @Override
    public void deleteAll() {
        tasksMap.clear();
    }

    @Override
    public Task createNewTask(String name, String description) {
        Task task = new Task(++taskId, name, description);
        tasksMap.put(taskId, task);
        return task;
    }

    @Override
    public Task createNewSubtask(String name, String description, Epic epic) {
        Task subTask = new SubTask(++taskId, name, description, epic);
        tasksMap.put(taskId, subTask);
        epic.addNewSubTaskId(taskId);
        return subTask;
    }

    @Override
    public Task createNewEpic(String name, String description) {
        Task epic = new Epic(++taskId, name, description);
        tasksMap.put(taskId, epic);
        return epic;
    }

    @Override
    public void getTask(Integer id) {
        Task tempTask = tasksMap.get(id);
        if (tempTask != null) {
            System.out.println(tasksMap.get(id));
            inMemoryHistoryManager.add(tasksMap.get(id));
        } else {
            System.out.println("Такой задачи нет\n");
        }
    }

    @Override
    public void updateTask(Task task, String name, String description, Status status) {
        switch(task.getTypeTask()){
            case TASK:
                int taskId = task.getId();
                task = new Task(task, name, description, status);
                tasksMap.replace(taskId, task);
                break;
            case SUB_TASK:
                int subtaskId = task.getId();
                task = new SubTask((SubTask) task, name, description, status);
                tasksMap.replace(subtaskId, task);
                int epicId = ((SubTask) task).getEpicId();
                refreshStatus(epicId);
                break;
            case EPIC:
                epicId = task.getId();
                task = new Epic((Epic) task, name, description);
                tasksMap.replace(epicId, task);
                break;
        }
    }

    @Override
    public void deleteWithId(int id) {
        if (tasksMap.get(id) != null) {
            if (tasksMap.get(id) instanceof Epic) {
                Epic epic = (Epic) tasksMap.get(id);
                List<Integer> subTaskList = epic.getSubTaskListId();
                for (int idSubTask : subTaskList) {
                    tasksMap.remove(idSubTask);
                }
            } else if (tasksMap.get(id) instanceof SubTask) {
                int epicId = ((SubTask) tasksMap.get(id)).getEpicId();
                ((Epic) tasksMap.get(epicId)).removeSubtaskId(id);   //удаляем в списке подзадач Epic подзадачу
                int subtaskId = tasksMap.get(id).getId();
                refreshStatus(epicId);
                tasksMap.remove(id);                                //удаляем подзадачу из менеджера
            }
            tasksMap.remove(id);
        } else {
            System.out.println("Такой задачи не существует");
        }
    }

    @Override
    public void allSubtaskFromEpic(Epic epic) {
        for (Integer i : epic.getSubTaskListId()) {
            tasksMap.get(i).toString();
        }
    }

    private void refreshStatus(int epicId) {
        List<Integer> subTasksId = ((Epic) tasksMap.get(epicId)).getSubTaskListId();
        int doneTasks = 0;
        int newTasks = 0;
        for (Integer subTask : subTasksId) {
            if (tasksMap.get(subTask).getStatus() == Status.DONE) {
                doneTasks++;
            } else if (tasksMap.get(subTask).getStatus() == Status.NEW) {
                newTasks++;
            }
        }
        Epic tmpEpic = (Epic) tasksMap.get(epicId);
        if (subTasksId.size() == doneTasks) {
            tmpEpic.setStatus(Status.DONE);
        } else if (subTasksId.size() == newTasks) {
            tmpEpic.setStatus(Status.NEW);
        } else {
            tmpEpic.setStatus(Status.IN_PROGRESS);
        }
    }
}