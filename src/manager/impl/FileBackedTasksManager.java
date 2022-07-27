package manager.impl;

import manager.interfaces.HistoryManager;
import taskclass.Epic;
import taskclass.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager  {
    private Path path;
    public FileBackedTasksManager(Path path) {
        super();
        this.path = path;
    }

    @Override
    public Task createNewTask(String name, String description) {
        Task tmpTask = super.createNewTask(name, description);
        save();
        return tmpTask;
    }

    @Override
    public Task createNewSubtask(String name, String description, Epic epic) {
        Task tmpTask = super.createNewSubtask(name, description, epic);
        save();
        return tmpTask;
    }

    @Override
    public Task createNewEpic(String name, String description) {
        Task tmpTask = super.createNewEpic(name, description);
        save();
        return tmpTask;
    }

    @Override
    public void getTask(Integer id) {
        super.getTask(id);
        save();
    }

    private void save(){
        if(!Files.exists(path)){
            try {
                Files.createFile(path);
                try(FileWriter fw = new FileWriter(path.toFile())){
                    fw.write("id,type,name,status,description,epic");
                }
            } catch (IOException e) {
                System.out.println("Проблема создания файла данных на ПЗУ");
                e.getStackTrace();
            }
        }else{
            try(FileWriter fw = new FileWriter(path.toFile())){
                String newLine;
                for(Map.Entry<Integer, Task> task: tasksMap.entrySet()){
                    newLine = toString(task.getValue());
                    fw.write(newLine);
                }

                fw.write(toStringHistory());
            } catch (IOException e) {
                System.out.println("Проблема заполнения файла с задачами на ПЗУ");
                e.getStackTrace();
            }
        }
    }

    public String toString(Task task) {
        //id,type,name,status,description,epic
        switch (task.getTypeTask()){
            case EPIC:
                return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDescription() + "," + "sub task - " +
                        ((Epic)task).getSubTaskListId().toString() + "\n";
            case TASK:
            case SUB_TASK:
                return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDescription() + "\n";

        }
        return "У задачи неправильный тип";
    }

    public String toStringHistory(){
        List<Task> taskList = new ArrayList<>(getInMemoryHistoryManager().getHistory());
        List<String> idTasks = new LinkedList<>();
        for(Task task: taskList){
            idTasks.add(String.valueOf(task.getId()));
        }
        return "\n" + idTasks.toString();
    }

    Task fromString(String value){
        String[] taskMetadate = value.split(",");           //возможно трабла с последней запятой которой нет

    }

    static FileBackedTasksManager loadFromFile(Path file){

    }

    public static void main(String[] args) {
        FileBackedTasksManager fbtm1 = new FileBackedTasksManager(
                Path.of("D:\\yp\\java-kanban\\src\\data\\saveData1.txt"));
        fbtm1.createNewTask("Вынести мусор к машине", "Машина приезжает с 6 до 7");
        fbtm1.createNewTask("Убраться к приезду родителей в гостинной",
                "Все моечные средства в кладовке");
        Epic epic1 = (Epic)fbtm1.createNewEpic("Расчет стоимости проекта дома", "Срок до 15.03.2022");
        fbtm1.createNewSubtask("Разработка архитектурного решения",
                "Срок до 03.02.2022", epic1);
        fbtm1.createNewSubtask("Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", epic1);
        fbtm1.createNewSubtask("Раcчет вентиляции",
                "Срок до 08.02.2022", epic1);

        Epic epic2 = (Epic)fbtm1.createNewEpic("Формирование списка мебели", "Срок до 17.06.2022");

        fbtm1.getTask(2);
        fbtm1.getTask(4);
        fbtm1.getTask(7);
        fbtm1.getTask(4);
        fbtm1.getTask(2);
        fbtm1.getTask(1);
    }
}
