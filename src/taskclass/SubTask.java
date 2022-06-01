package taskclass;

import enumclass.Status;
import enumclass.TypeTask;

public class SubTask extends Task{

    private int EpicId;

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
        return TypeTask.SUB_TASK.getName();
    }

    @Override
    public String toString() {
        return getTypeTask() + ":" + "\t" + getName() + "\n"
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
}