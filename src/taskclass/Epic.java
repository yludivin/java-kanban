package taskclass;

import enumclass.Status;
import enumclass.TypeTask;
import manager.interfaces.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTaskListId;

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
        this.subTaskListId = new ArrayList<>();
    }

    public LocalDateTime getEndTime(TaskManager taskManager) {
        LocalDateTime latestSubtask = LocalDateTime.MIN;
        for (Integer integer : subTaskListId) {
            if(taskManager.getTaskMap().get(integer).getEndTime().isAfter(latestSubtask)){
                latestSubtask = taskManager.getTaskMap().get(integer).getEndTime();
            }
        }
        return latestSubtask;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public void setSubTaskListId(List<Integer> subTaskListId) {
        this.subTaskListId = subTaskListId;
    }

    public void addNewSubTaskId(Integer id) {
        subTaskListId.add(id);
    }

    public List<Integer> getSubTaskListId() {
        return subTaskListId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void removeSubtaskId(Integer id) {
        subTaskListId.remove(subTaskListId.indexOf(id));
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " + getTypeTask().getName() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n"
                + "Завершение:" + getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}