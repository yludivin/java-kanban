package taskclass;

import enumclass.Status;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected Status status;
    private String typeTask = "Задача";


    public Task(){

    }

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(Task obj, String name, String description, Status status){
        this.id = obj.getId();
        this.name = name;
        this.description = description;
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

    public Integer getId() {
        return id;
    }

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
        Task task = (Task) obj;
        return (this.id == task.id) &&
                Objects.equals(this.name, task.name) &&
                Objects.equals(this.description, task.description) &&
                (this.status == task.getStatus()) &&
                (this.typeTask == task.typeTask);
    }
}
