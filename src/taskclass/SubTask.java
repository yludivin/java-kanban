package taskclass;

import enumclass.Status;

import java.util.Objects;

public class SubTask extends Task{
    private Integer EpicId;
    private String typeTask = "Подзадача";

    public SubTask(Integer id, String name, String description, Epic epic) {
        super(id, name, description);
        this.EpicId = epic.getId();
    }

    public SubTask(SubTask obj, String name, String description, Status status){
        super(obj, name, description, status);
        this.EpicId = obj.EpicId;
    }

    public Integer getEpicId() {
        return EpicId;
    }

    @Override
    public String getTypeTask() {
        return this.typeTask;
    }

    @Override
    public String toString() {
        return getTypeTask() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n";
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SubTask task = (SubTask) obj;
        return (id == task.id) &&
                Objects.equals(this.name, task.name) &&
                Objects.equals(this.description, task.description) &&
                (this.status == task.getStatus()) &&
                (this.EpicId == task.getEpicId()) &&
                (this.typeTask == task.typeTask);
    }
}
