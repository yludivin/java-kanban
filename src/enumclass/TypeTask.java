package enumclass;

public enum TypeTask {
    TASK("Задача"),
    SUB_TASK("Подзадача"),
    EPIC("Эпик");

    private String name;

    TypeTask(String n) {
        name = n;
    }
    public String getName(){
        return name;
    }
}
