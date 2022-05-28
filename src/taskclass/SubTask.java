package taskclass;

public class SubTask extends Task{
    Integer EpicId;

    public SubTask(Integer id, String name, String description, Epic epic) {
        super(id, name, description);
        this.EpicId = epic.getId();
    }

    public SubTask(SubTask obj, String name, String description){
        super(obj, name, description);
    }
}
