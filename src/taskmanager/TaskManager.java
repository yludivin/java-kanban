package taskmanager;

import enumclass.Status;
import taskclass.*;

import java.util.ArrayList;
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

    public SubTask createNewSubtask(String name, String description, Epic epic){
        Task subTask = new SubTask(++taskId, name, description, epic);
        list.add(subTask);
        epic.addNewSubTaskId(taskId);
        return (SubTask) subTask;
    }

    public Epic createNewEpic(String name, String description){
        Task epic = new Epic(++taskId, name, description);
        list.add(epic);
        return (Epic) epic;
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

    public void deleteWithId(Integer id){   //тут все плохо
        for(Task task : list) {
            if (id.equals(task.getId())) {
                list.set(task.getId(), null);
                if (task instanceof Epic) {
                    for (Integer subtaskId : ((Epic) task).getSubTaskListId()) {
                        for(Task subtask : list){
                            if(subtask.getId() == subtaskId){
                                list.remove(subtask);
                                break;
                            }
                        }
                    }
                    refreshStatus();
                    return;
                }
                if (task instanceof SubTask) {
                    int epicId = ((SubTask) task).getEpicId();
                    for(Task tempEpic : list){
                        if(task.getId() == epicId){
                            ((Epic) tempEpic).removeSubtuskId(id);
                        }
                    }
                    refreshStatus();
                    return;
                }
                return;
            }
        }
    }
    public void allSubtaskFromEpic(Epic epic){
        for(Integer i : epic.getSubTaskListId()){
            list.get(i).toString();
        }
    }

    private void refreshStatus(){
        for(Task task : list){
            if(task instanceof Epic){
                if(((Epic) task).getSubTaskListId().isEmpty()){
                    ((Epic) task).setStatus(Status.NEW);
                    return;
                }
                int newStatus = 0;
                int doneStatus = 0;
                for (int i = 0; i < ((Epic) task).getSubTaskListId().size(); i++){
                    if(Status.NEW == list.get(i).getStatus()){
                        newStatus++;
                    }
                    if(Status.DONE == list.get(i).getStatus()){
                        doneStatus++;
                    }
                }
                if(doneStatus == ((Epic) task).getSubTaskListId().size()){
                    ((Epic) task).setStatus(Status.DONE);
                }else if(newStatus == ((Epic) task).getSubTaskListId().size()){
                    ((Epic) task).setStatus(Status.NEW);
                }else{
                    ((Epic) task).setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }
}
