package enumclass;

public enum TypeTask {
    TASK("Задача"),
    SUB_TASK("Подзадача"),
    EPIC("Эпик");

    private String name;

    TypeTask(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
