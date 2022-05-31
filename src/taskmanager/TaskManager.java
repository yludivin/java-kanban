package taskmanager;

import enumclass.Status;
import taskclass.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskManager {
    private List<Task> list;
    private Integer taskId;

    public TaskManager() {
        this.list = new ArrayList<>();
        taskId = 0;
    }

    public void getAllTasksName(){
        for(Task task : list){
            System.out.println(task.toString());
        }
    }

    public void deleteAll(){
        list.clear();
    }

    public Task createNewTask(String name, String description){
        Task task = new Task(++taskId, name, description);
        list.add(task);
        return task;
    }

    public Task createNewSubtask(String name, String description, Epic epic){
        Task subTask = new SubTask(++taskId, name, description, epic);
        list.add(subTask);
        epic.addNewSubTaskId(taskId);
        return subTask;
    }

    public Task createNewEpic(String name, String description){
        Task epic = new Epic(++taskId, name, description);
        list.add(epic);
        return epic;
    }

    public void showTaskWithId(Integer id){
        for(Task task : list){
            if(id.equals(task.getId())){
                task.toString();
                return;
            }
        }
    }

    public void updateTask(Task abstractTask, String name, String description, Status status){
        if(abstractTask instanceof Epic) {
            int index = list.indexOf(abstractTask);
            abstractTask = new Epic((Epic)abstractTask, name, description);
            list.set(index, abstractTask);
        }else if (abstractTask instanceof SubTask){
            int index = list.indexOf(abstractTask);
            abstractTask = new SubTask((SubTask)abstractTask, name, description, status);
            list.set(index, abstractTask);
            refreshStatus();
        }else{
            int index = list.indexOf(abstractTask);
            abstractTask = new Task(abstractTask, name, description, status);
            list.set(index, abstractTask);
        }
    }

    public void deleteWithId(int id){   //тут все плохо
        Task tmp = null;
        for (Task task : list) {
            if (task.getId() == id) {
                tmp = task;
                break;
            }
        }
        if (tmp != null) {
            if (tmp instanceof Epic) {
                Epic epic = (Epic) tmp;
                List<Integer> subTaskList = epic.getSubTaskListId();
                for (int idSubTask : subTaskList) {
                    Iterator<Task> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().getId() == idSubTask) {
                            iterator.remove();
                        }
                    }
                }
            } else if (tmp instanceof SubTask) {
                SubTask subTask = (SubTask) tmp;
                Epic epic = (Epic)list.stream().filter(task -> task.getId() == subTask.getEpicId()).findFirst().get();
                epic.removeSubtaskId(subTask.getId());
            }
            list.remove(tmp);
        }
    }

    public void allSubtaskFromEpic(Epic epic){
        for(Integer i : epic.getSubTaskListId()){
            list.get(i).toString();
        }
    }

    private void refreshStatus(){
        Epic tmpEpic;
        for(Task task : list){
            if(task instanceof Epic){
                tmpEpic = (Epic)task;
                List<Integer> subTasksId = tmpEpic.getSubTaskListId();
                if(subTasksId.isEmpty()){
                    tmpEpic.setStatus(Status.NEW);
                }else{
                    int doneTasks = 0;
                    int newTasks = 0;
                    for(Task findSubtask : list){
                        for (Integer subTaskNumber : subTasksId){
                            if(findSubtask.getId() == subTaskNumber){
                                if(findSubtask.getStatus() == Status.NEW){
                                    ++newTasks;
                                }
                                if(findSubtask.getStatus() == Status.DONE){
                                    ++doneTasks;
                                }
                            }
                        }
                    }
                    if(subTasksId.size() == doneTasks){
                        tmpEpic.setStatus(Status.DONE);
                    }else if(subTasksId.size() == newTasks){
                        tmpEpic.setStatus(Status.NEW);
                    }else{
                        tmpEpic.setStatus(Status.IN_PROGRESS);
                    }
                }
            }
        }
    }
}