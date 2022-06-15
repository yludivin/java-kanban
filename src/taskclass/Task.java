package taskclass;

import enumclass.Status;
import enumclass.TypeTask;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;

    public Task() {
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(Task obj, String name, String description, Status status) {
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

    public int getId() {
        return id;
    }

    public String getTypeTask() {
        return TypeTask.TASK.getName();
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " + getTypeTask() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}