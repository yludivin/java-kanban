package manager.impl;

import api.KVServer;
import api.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HTTPTaskManager extends FileBackedTasksManager {
    protected Map<Integer, Task> tasksMap;
    protected Map<Integer, Task> listOfTasks;
    protected Map<Integer, Epic> listOfEpics;
    protected Map<Integer, SubTask> listOfSubTasks;
    private final KVTaskClient taskClient;
    private final Gson gson;
    private final Type taskUserType = new TypeToken<Map<Integer, Task>>() {
    }.getType();
    private final Type epicTaskUserType = new TypeToken<Map<Integer, Epic>>() {
    }.getType();
    private final Type subTaskUserType = new TypeToken<Map<Integer, SubTask>>() {
    }.getType();
    private final Type historyUserType = new TypeToken<List<Task>>() {
    }.getType();

    public HTTPTaskManager(URL url) {
        super("resources\\HTTPTaskManager.csv");
        taskClient = new KVTaskClient(url);
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    public static void main(String[] args) throws IOException {
        KVServer server = new KVServer();
        server.start();
        URL url = new URL("http://localhost:8078/");
        HTTPTaskManager taskManager = new HTTPTaskManager(url);
        Task task1 = new Task("1", "1","14:00 13-07-2020",80);
        taskManager.createNewTask(task1);
        Epic epic2 = new Epic("2", "2");
        taskManager.createNewEpic(epic2);
        SubTask subTask3 = new SubTask("3", "3","14:00 14-07-2020",60,epic2);
        taskManager.createNewSubtask(subTask3);
    }

    @Override
    public void save() {
        String tasks = gson.toJson(getOnlyTasks(), taskUserType);
        String epicTasks = gson.toJson(getOnlyEpics(), epicTaskUserType);
        String subTasks = gson.toJson(getOnlySubtasks(), subTaskUserType);
        String history = gson.toJson(getInMemoryHistoryManager().getHistory(), historyUserType);

        try {
            taskClient.put("tasks", tasks);
            taskClient.put("epicTasks", epicTasks);
            taskClient.put("subTasks", subTasks);
            taskClient.put("history", history);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void loadFromFile(String name) {
        deleteAllTasks();
        try {
            listOfTasks = gson.fromJson(taskClient.load(name + "tasks"), taskUserType);
            listOfEpics = gson.fromJson(taskClient.load(name + "epicTasks"), epicTaskUserType);
            listOfSubTasks = gson.fromJson(taskClient.load(name + "subTasks"), subTaskUserType);
            tasksMap.putAll(listOfTasks);
            tasksMap.putAll(listOfEpics);
            tasksMap.putAll(listOfSubTasks);
            List<Task> history = gson.fromJson(taskClient.load(name + "history"), historyUserType);
            for (Task task : history) {
                if (listOfTasks.containsValue(task)) {
                    inMemoryHistoryManager.add(task);
                } else if (listOfEpics.containsKey(task.getId())) {
                    inMemoryHistoryManager.add(task);
                } else {
                    inMemoryHistoryManager.add(task);
                }
            }
        } catch(IOException | InterruptedException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
