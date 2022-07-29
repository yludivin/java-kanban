package manager.impl;

import enumclass.Status;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import manager.Managers;
import taskclass.Epic;
import taskclass.SubTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, taskclass.Task> tasksMap;
    protected Integer taskId;
    protected HistoryManager inMemoryHistoryManager;

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
        for (Map.Entry<Integer, taskclass.Task> task : tasksMap.entrySet()) {
            System.out.println(task.toString());
        }
    }

    public void fromStringToNodeMap(List<String> tasksOrder){
        for(int i = 0; i < tasksOrder.size(); i++){


        }
    }

    @Override
    public void deleteAll() {
        tasksMap.clear();
    }

    @Override
    public taskclass.Task createNewTask(String name, String description) {
        taskclass.Task task = new taskclass.Task(++taskId, name, description);
        tasksMap.put(taskId, task);
        return task;
    }

    public taskclass.Task createNewSubtask(String name, String description) {       //для создания подзадачи
        taskclass.Task subTask = new SubTask(++taskId, name, description);          //из строки
        tasksMap.put(taskId, subTask);
        return subTask;
    }
    
    @Override
    public taskclass.Task createNewSubtask(String name, String description, Epic epic) {
        taskclass.Task subTask = new SubTask(++taskId, name, description, epic);
        tasksMap.put(taskId, subTask);
        epic.addNewSubTaskId(taskId);
        return subTask;
    }

    @Override
    public taskclass.Task createNewEpic(String name, String description) {
        taskclass.Task epic = new Epic(++taskId, name, description);
        tasksMap.put(taskId, epic);
        return epic;
    }

    @Override
    public void getTask(Integer id) {
        taskclass.Task tempTask = tasksMap.get(id);
        if (tempTask != null) {
            //System.out.println(tasksMap.get(id));
            inMemoryHistoryManager.add(tasksMap.get(id));
        } else {
            System.out.println("Такой задачи нет\n");
        }
    }

    @Override
    public void updateTask (taskclass.Task task, String name, String description, Status status) {
        switch(task.getTypeTask()){
            case TASK:
            case EPIC:
                task.setName(name);
                task.setDescription(description);
                task.setStatus(status);
                break;
            case SUB_TASK:
                task.setName(name);
                task.setDescription(description);
                task.setStatus(status);
                int epicId = ((SubTask) task).getEpicId();
                refreshStatus(epicId);      //после создания статус эпика зависит от статусов сабтасков
                break;                      //у всех остальных типов задач выставляется вручную
        }
    }

    @Override
    public void deleteWithId(int id) {
        if (tasksMap.get(id) != null) {
            switch ((tasksMap.get(id)).getTypeTask()){
                case EPIC:
                    Epic epic = (Epic) tasksMap.get(id);
                    List<Integer> subTaskList = epic.getSubTaskListId();
                    for (int idSubTask : subTaskList) {
                        tasksMap.remove(idSubTask);
                    }
                    break;
                case SUB_TASK:
                    int epicId = ((SubTask) tasksMap.get(id)).getEpicId();
                    ((Epic) tasksMap.get(epicId)).removeSubtaskId(id);   //удаляем в списке подзадач Epic подзадачу
                    int subtaskId = tasksMap.get(id).getId();
                    refreshStatus(epicId);
                    tasksMap.remove(subtaskId);
                    break;
                default:
                    tasksMap.remove(id);
            }
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

    protected void refreshStatus(int epicId) {
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