package taskclass;

import enumclass.Status;
import enumclass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected Status status;
    protected Duration duration = Duration.ZERO;
    protected LocalDateTime startTime = LocalDateTime.MIN;
    protected LocalDateTime endTime = LocalDateTime.MIN;
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");


    public Task(String name, String description, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = LocalDateTime.parse(startTime, dateTimeFormatter);
        this.duration = Duration.ofMinutes(duration);
        this.endTime = this.startTime.plusMinutes(duration);
    }

/*    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }*/

    protected Task() {
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(int minutes){
        this.duration = Duration.ofMinutes(minutes);
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void calculateEndTime() {
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public TypeTask getTypeTask() {
        return TypeTask.TASK;
    }

    public void setId(int id){
        this.id = id;
    }


    @Override
    public String toString() {
        String plug = "Время не установлено";
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}