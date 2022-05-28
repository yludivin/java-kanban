package taskclass;

import java.util.List;

public class Epic extends Task {

    private List<Integer> subTaskListId;

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
    }

    public Epic(Epic obj, String name, String description){
        super(obj, name, description);
    }

    public Integer getId(){
        return this.getId();
    }

    public void addNewSubTaskId(Integer id) {
        subTaskListId.add(id);
    }
}
