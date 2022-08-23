package taskclass;

import enumclass.Status;
import enumclass.TypeTask;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public LocalDateTime getEndTime(){
        return startTime.plusMinutes(duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
        return "ID: " + getId() + " " + getTypeTask().getName() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n"
                + "Завершение:" + getEndTime();
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