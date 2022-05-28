package taskmanager;

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
            task.toString();
        }
    }

    public void deleteAll(){
        list.clear();
    }

    public void createNewTask(String name, String description){
        Task task = new Task(++taskId, name, description);
        list.add(task);
    }

    public void createNewSubtask(String name, String description, Epic epic){
        Task subTask = new SubTask(++taskId, name, description, epic);
        list.add(subTask);
        epic.addNewSubTaskId(subTask.getId());
    }

    public void createNewEpic(String name, String description){
        Task epic = new Epic(++taskId, name, description);
        list.add(epic);
    }

    public void showTaskWithId(Integer id){
        for(Task task : list){
            if(id.equals(task.getId())){
                task.toString();
            }
        }
    }

    public void refreshAnyTask(Task abstractTask, String name, String description){
        Task task = new Task(abstractTask, name, description);

    }


}
