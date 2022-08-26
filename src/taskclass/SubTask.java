package taskclass;

import enumclass.Status;
import enumclass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description,String startTime, long duration, Epic epic) {
        super(name, description,startTime,duration);
        this.epicId = epic.getId();
    }

    public SubTask(String name, String description, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = LocalDateTime.parse(startTime, dateTimeFormatter);
        this.duration = Duration.ofMinutes(duration);
        this.endTime = this.startTime.plusMinutes(duration);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.SUB_TASK;
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
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int getId() {
        return this.id;
    }
}