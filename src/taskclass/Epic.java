package taskclass;

import enumclass.Status;
import enumclass.TypeTask;
import manager.interfaces.TaskManager;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTaskListId;
    LocalDateTime endTime = LocalDateTime.of(1970, Month.JANUARY,1,0,0);


    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        subTaskListId = new ArrayList<>();
    }


    public void calculateEndTime(TaskManager taskManager) {
        LocalDateTime endTime = LocalDateTime.MIN;
        for (Integer integer : subTaskListId) {
            if(taskManager.getTaskMap().get(integer).getEndTime().isAfter(endTime)){
                endTime = taskManager.getTaskMap().get(integer).getEndTime();
            }
        }
        this.endTime = endTime;
    }

    public void calculateStartTime(TaskManager taskManager){
        LocalDateTime startTime = LocalDateTime.MAX;
        for (Integer integer : subTaskListId) {
            if(taskManager.getTaskMap().get(integer).getStartTime().isBefore(startTime)){
                startTime = taskManager.getTaskMap().get(integer).getStartTime();
            }
        }
        this.startTime = startTime;
    }

    public void calculateDuration(){
        this.duration = Duration.between(this.startTime, this.endTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
        String plug = "Эпик без подзадач";
        String startWork = (getStartTime() == LocalDateTime.MIN) ? plug : dateTimeFormatter.format(getStartTime());
        String endWork = (getEndTime() == LocalDateTime.MIN) ? plug : dateTimeFormatter.format(getEndTime());
        String duration = (getDuration() == Duration.ZERO) ? plug : getDuration().toMinutes() + "";
        return "ID: " + getId() + " " + getTypeTask().getName() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n"
                + "Начало работы:\t" + startWork + "\n"
                + "Продолжительность(мин):\t" + duration + "\n"
                + "Завершение:\t" + endWork + "\n";
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