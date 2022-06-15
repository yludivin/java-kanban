package taskclass;

import enumclass.Status;
import enumclass.TypeTask;

public class SubTask extends Task {

    private int epicId;

    public SubTask(Integer id, String name, String description, Epic epic) {
        super(id, name, description);
        this.epicId = epic.getId();
    }

    public SubTask(SubTask obj, String name, String description, Status status) {
        super(obj, name, description, status);
        this.epicId = obj.epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String getTypeTask() {
        return TypeTask.SUB_TASK.getName();
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " + getTypeTask() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n";
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