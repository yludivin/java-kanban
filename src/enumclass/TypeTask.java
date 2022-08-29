package enumclass;

public enum TypeTask {
    TASK("Задача"),
    SUBTASK("Подзадача"),
    EPIC("Эпик");

    private final String name;

    TypeTask(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
