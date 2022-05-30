package taskclass;

import enumclass.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> subTaskListId;
    private String typeTask = "Эпик";

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
        subTaskListId = new ArrayList<>();
    }

    public Epic(Epic obj, String name, String description){
        this.id = obj.getId();
        this.name = name;
        this.description = description;
        this.subTaskListId = obj.subTaskListId;
    }

    public Integer getId(){
        return this.id;
    }

    public void addNewSubTaskId(Integer id) {
        subTaskListId.add(id);
    }

    public List<Integer> getSubTaskListId() {
        return subTaskListId;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public void removeSubtuskId(Integer id){
        subTaskListId.remove(subTaskListId.indexOf(id));
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
        return Objects.hash(id, name, description, status, subTaskListId, typeTask);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Epic task = (Epic) obj;
        return (this.id == task.id) &&
                Objects.equals(this.name, task.name) &&
                Objects.equals(this.description, task.description) &&
                (this.status == task.getStatus()) &&
                Objects.equals(this.subTaskListId, task.subTaskListId) &&
                Objects.equals(this.typeTask, task.typeTask);
    }
}
