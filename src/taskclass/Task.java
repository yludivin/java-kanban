package taskclass;

import enumclass.Status;

import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private Status status;

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(Task obj, String name, String description){
        this.id = obj.getId();
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return getName() + ":" + "\t" + getName() + "\n"
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
        return (id == task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                (status == task.getStatus());
    }
}
