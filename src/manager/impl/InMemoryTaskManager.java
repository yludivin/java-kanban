package manager.impl;

import enumclass.Status;
import enumclass.TypeTask;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import manager.Managers;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasksMap;
    protected Map<Integer, taskclass.Task> EpicsMap;
    protected Map<Integer, taskclass.Task> SubTasksMap;
    protected Integer taskId;
    protected HistoryManager inMemoryHistoryManager;
    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.taskId = 0;
        this.inMemoryHistoryManager = Managers.getDefaultHistory();
        this.prioritizedTasks = new TreeSet<>(timeComporator);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void refreshPrioritizedTasks() {
        prioritizedTasks.clear();
        for (Task value : tasksMap.values()) {
            if (value.getTypeTask() != TypeTask.EPIC) {
                prioritizedTasks.add(value);
            }
        }
    }

    protected void validateTask() {
        LocalDateTime endTimePrevTask = LocalDateTime.MIN;
        for (Task prioritizedTask : prioritizedTasks) {
            if (prioritizedTask.getStartTime().isBefore(endTimePrevTask)) {
                throw new IllegalArgumentException("Выявлено пересечение " +
                        "времени выполнения задачи с уже существующими задачами");
            }
            endTimePrevTask = prioritizedTask.getEndTime();
        }
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    public void setInMemoryHistoryManager(HistoryManager inMemoryHistoryManager) {
        this.inMemoryHistoryManager = inMemoryHistoryManager;
    }

    @Override
    public void getTasks() {
        for (Map.Entry<Integer, taskclass.Task> task : tasksMap.entrySet()) {
            System.out.println(task.toString());
        }
    }


    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
        refreshPrioritizedTasks();
    }

    @Override
    public Task createNewTask(Task task) {
        int id = ++taskId;
        task.setId(id);
        tasksMap.put(taskId, task);
        addTaskToPriortizedSet(task);
        validateTask();
        return task;
    }

    @Override
    public Task createNewSubtask(SubTask subTask) {
        int id = ++taskId;
        subTask.setId(id);
        tasksMap.put(taskId, subTask);
        ((Epic) tasksMap.get(subTask.getEpicId())).addNewSubTaskId(taskId);
        addTaskToPriortizedSet(subTask);
        refreshEpicLastTime((tasksMap.get(subTask.getEpicId())).getId());
        refreshEpicStartTime((tasksMap.get(subTask.getEpicId())).getId());
        refreshEpicDuration((tasksMap.get(subTask.getEpicId())).getId());
        validateTask();
        return subTask;
    }

    @Override
    public Task createNewEpic(Epic epic) {
        int id = ++taskId;
        epic.setId(id);
        tasksMap.put(taskId, epic);
        return epic;
    }

    @Override
    public void getTaskById(Integer id) {
        taskclass.Task tempTask = tasksMap.get(id);
        if (tempTask != null) {
            inMemoryHistoryManager.add(tasksMap.get(id));
        } else {
            System.out.println("Такой задачи нет\n");
        }
    }

    @Override
    public void updateTask(Task oldTask, Task newTask) {
        switch (oldTask.getTypeTask()) {
            case TASK:
                oldTask.setName(newTask.getName());
                oldTask.setDescription(newTask.getDescription());
                oldTask.setStatus(newTask.getStatus());
                oldTask.setStartTime(newTask.getStartTime());
                oldTask.setDuration((int) newTask.getDuration().toMinutes());
                oldTask.calculateEndTime();
                refreshPrioritizedTasks();
                validateTask();
                break;
            case EPIC:
                oldTask.setName(newTask.getName());
                oldTask.setDescription(newTask.getDescription());
                break;
            case SUBTASK:
                oldTask.setName(newTask.getName());
                oldTask.setDescription(newTask.getDescription());
                oldTask.setStatus(newTask.getStatus());
                oldTask.setStartTime(newTask.getStartTime());
                oldTask.setDuration((int) newTask.getDuration().toMinutes());
                oldTask.calculateEndTime();
                refreshPrioritizedTasks();
                validateTask();
                int epicId = ((SubTask) oldTask).getEpicId();
                refreshEpicStatus(epicId);
                refreshEpicLastTime(epicId);
                refreshEpicStartTime(epicId);
                refreshEpicDuration(epicId);
                break;
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasksMap.get(id) != null) {
            switch ((tasksMap.get(id)).getTypeTask()) {
                case EPIC:
                    Epic epic = (Epic) tasksMap.get(id);
                    List<Integer> subTaskList = epic.getSubTaskListId();
                    for (int idSubTask : subTaskList) {
                        tasksMap.remove(idSubTask);
                    }
                    break;
                case SUBTASK:
                    int epicId = ((SubTask) tasksMap.get(id)).getEpicId();
                    ((Epic) tasksMap.get(epicId)).removeSubtaskId(id);   //удаляем в списке подзадач Epic подзадачу
                    int subtaskId = tasksMap.get(id).getId();
                    refreshEpicStatus(epicId);
                    refreshEpicLastTime(epicId);
                    refreshEpicStartTime(epicId);
                    tasksMap.remove(subtaskId);
                    break;
                default:
                    tasksMap.remove(id);
            }
            prioritizedTasks.remove(tasksMap.get(id));
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

    protected void refreshEpicStatus(int epicId) {
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

    public void refreshEpicLastTime(int epicId) {
        ((Epic) tasksMap.get(epicId)).calculateEndTime(this);
    }

    public void refreshEpicStartTime(int epicId) {
        ((Epic) tasksMap.get(epicId)).calculateStartTime(this);
    }

    public void refreshEpicDuration(int epicId) {
        ((Epic) tasksMap.get(epicId)).calculateDuration();
    }

    @Override
    public Integer getTasksQuantity() {
        return tasksMap.size();
    }

    @Override
    public Map<Integer, Task> getTaskMap() {
        return tasksMap;
    }

    void addTaskToPriortizedSet(Task task) {
        if (!prioritizedTasks.add(task)) {        //если не добавился в сет, значит уже есть такая
            throw new IllegalArgumentException("В это время уже начинается другая задача ");
        }
    }

    Comparator<Task> timeComporator = (o1, o2) -> {
        try {                                //когда пробегается при 1 задаче в тестах при удалении по Id
            if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            }
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    };
}