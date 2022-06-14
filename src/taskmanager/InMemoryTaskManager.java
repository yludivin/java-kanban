package taskmanager;

import enumclass.Status;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasksMap;
    private Integer taskId;

    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.taskId = 0;
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
    public void showTaskWithId(Integer id) {
        System.out.println(tasksMap.get(id));
    }

    @Override
    public void updateTask(Task abstractTask, String name, String description, Status status) {
        if (abstractTask instanceof Epic) {
            int taskId = abstractTask.getId();
            abstractTask = new Epic((Epic) abstractTask, name, description);
            tasksMap.replace(taskId, abstractTask);
        } else if (abstractTask instanceof SubTask) {
            int taskId = abstractTask.getId();
            abstractTask = new SubTask((SubTask) abstractTask, name, description, status);
            tasksMap.replace(taskId, abstractTask);
            refreshStatus();
        } else {
            int taskId = abstractTask.getId();
            abstractTask = new Task(abstractTask, name, description, status);
            tasksMap.replace(taskId, abstractTask);
        }
    }

    @Override
    public void deleteWithId(int id) {

        if (tasksMap.remove(id) != null) {      //удаляем задачу и проверяем, что она существует
            if (tasksMap.get(id) instanceof Epic) {
                Epic epic = (Epic) tasksMap.get(id);
                List<Integer> subTaskList = epic.getSubTaskListId();
                for (int idSubTask : subTaskList) {
                    Iterator<Task> iterator = tasksMap.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().getId() == idSubTask) {
                            iterator.remove();
                        }
                    }
                }
            } else if (tmp instanceof SubTask) {
                SubTask subTask = (SubTask) tmp;
                Epic epic = (Epic) tasksMap.stream().filter(task -> task.getId() == subTask.getEpicId()).findFirst().get();
                epic.removeSubtaskId(subTask.getId());
            }
            tasksMap.remove(tmp);
        }else {
            System.out.println("Такой задачи не существует");
        }
    }

    @Override
    public void allSubtaskFromEpic(Epic epic) {
        for (Integer i : epic.getSubTaskListId()) {
            tasksMap.get(i).toString();
        }
    }

    private void refreshStatus() {
        Epic tmpEpic;
        for (Task task : tasksMap) {
            if (task instanceof Epic) {
                tmpEpic = (Epic) task;
                List<Integer> subTasksId = tmpEpic.getSubTaskListId();
                if (subTasksId.isEmpty()) {
                    tmpEpic.setStatus(Status.NEW);
                } else {
                    int doneTasks = 0;
                    int newTasks = 0;
                    for (Task findSubtask : tasksMap) {
                        for (Integer subTaskNumber : subTasksId) {
                            if (findSubtask.getId() == subTaskNumber) {
                                if (findSubtask.getStatus() == Status.NEW) {
                                    ++newTasks;
                                }
                                if (findSubtask.getStatus() == Status.DONE) {
                                    ++doneTasks;
                                }
                            }
                        }
                    }
                    if (subTasksId.size() == doneTasks) {
                        tmpEpic.setStatus(Status.DONE);
                    } else if (subTasksId.size() == newTasks) {
                        tmpEpic.setStatus(Status.NEW);
                    } else {
                        tmpEpic.setStatus(Status.IN_PROGRESS);
                    }
                }
            }
        }
    }
}