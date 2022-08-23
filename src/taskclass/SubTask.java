package taskclass;

import enumclass.Status;
import enumclass.TypeTask;

public class SubTask extends Task {

    private int epicId;

    public SubTask(Integer id, String name, String description) {           //для создания подзадачи из строки
        super(id, name, description);
    }

    public SubTask(Integer id, String name, String description, Epic epic) {
        super(id, name, description);
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
        return TypeTask.SUB_TASK;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " + getTypeTask().getName() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n"
                +  "Завершение:" + getEndTime();
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