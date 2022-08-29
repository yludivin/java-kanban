package taskclass;

import enumclass.TypeTask;

import java.util.Objects;

public class SubTask extends Task {
    private Integer epicId;

    public SubTask(String name, String description,String startTime, long duration, Epic epic) {
        super(name, description,startTime,duration);
        this.epicId = epic.getId();
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " + getTypeTask().getName() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n"
                + "Начало работы:\t" + dateTimeFormatter.format(getStartTime()) + "\n"
                + "Продолжительность(мин):\t" + getDuration().toMinutes() + "\n"
                + "Завершение:\t" + dateTimeFormatter.format(getEndTime()) + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubTask anotherTask = (SubTask) o;

        return id.equals(anotherTask.getId()) &&
                name.equals(anotherTask.getName()) &&
                description.equals(anotherTask.getDescription()) &&
                status == anotherTask.getStatus() &&
                startTime.isEqual(anotherTask.getStartTime()) &&
                endTime.isEqual(anotherTask.getEndTime()) &&
                duration.equals(anotherTask.getDuration()) &&
                epicId.equals(anotherTask.getEpicId());
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(id, name, description, status, startTime, endTime, duration, epicId);
    }

    @Override
    public int getId() {
        return this.id;
    }
}