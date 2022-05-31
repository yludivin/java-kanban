package taskclass;

import enumclass.Status;

import java.util.ArrayList;
import java.util.List;

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

    public int getId(){
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

    public void removeSubtaskId(Integer id){
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
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}