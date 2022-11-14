package api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.interfaces.TaskManager;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private Gson gson;
    private HttpServer server;
    private static final int PORT = 8080;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        server.createContext("/tasks/", new TaskHandler());
        gson = new Gson();
        start();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println("HTTP-сервер запущен на порту: " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks/");

        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtasks", new SubtaskHandler());
        server.createContext("/tasks", new PrioritizedTasksHandler());
        server.createContext("/tasks/history", new HistoryHandler());
    }

    public void stop(){
        server.stop(0);
        System.out.println("HTTP-сервер остановлен на порту: " + PORT);
    }


    private class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            switch (metod){
                case "GET":
                    if(path.startsWith("/tasks/task?id=")) {
                        int id = parseInt(path.replaceFirst("/tasks/task?id=", ""));
                        Task task = taskManager.getTaskMap().get(id);
                        String taskJson = gson.toJson(task, Task.class);
                        sendText(httpExchange, taskJson);
                    }else if(path.startsWith("/tasks/task/")) {
                        String allTasks = gson.toJson(taskManager.getTaskMap());
                        sendText(httpExchange, allTasks);
                    }else {
                        throw new RuntimeException("Путь или запрос неправильны");
                    }
                    break;
                    case "DELETE":
                        if(Pattern.matches("^/tasks/task/?id=\\d+/", path)) {
                            int id = parseInt(path.replaceFirst("/tasks/task?id=", ""));
                            try {
                                taskManager.deleteTaskById(id);
                                System.out.println("Задача с id " + id + "удалена");
                                httpExchange.sendResponseHeaders(200,0);
                            } catch (NullPointerException e) {
                                throw new RuntimeException("Задачи с таким id не существует");
                            }
                        }else if(Pattern.matches("^/tasks/task/", path)) {
                            taskManager.deleteAllTasks();
                            System.out.println("Все задачи удалены");
                            httpExchange.sendResponseHeaders(200,0);
                        }else {
                            throw new RuntimeException("Путь или запрос неправильный");
                        }
                        break;

                case "POST":
                    InputStream is = httpExchange.getRequestBody();
                    String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(jsonString);
                    if(!jsonElement.isJsonObject()){
                        throw new RuntimeException("Неправильный обьект в теле запроса");
                    }else{
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Task task = gson.fromJson(jsonObject, Task.class);
                        taskManager.createNewTask(task);
                    }
                default:
                    throw new RuntimeException("Неправильный метод");
            }
        }
    }

    private class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            switch (metod){
                case "GET":
                    if(path.startsWith("/tasks/epic?id=")) {
                        int id = parseInt(path.replaceFirst("/tasks/epic?id=", ""));
                        Epic epic = (Epic)taskManager.getTaskMap().get(id);
                        String epicJson = gson.toJson(epic, Epic.class);
                        sendText(httpExchange, epicJson);
                    }else if(path.startsWith("/tasks/epic/")) {     //TODO убрать или сделать
                        String allTasks = gson.toJson(taskManager.getTaskMap());
                        sendText(httpExchange, allTasks);
                    }else {
                        throw new RuntimeException("Путь или запрос неправильны");
                    }
                    break;
                case "DELETE":
                    if(Pattern.matches("^/tasks/epic/?id=\\d+/", path)) {
                        int id = parseInt(path.replaceFirst("/tasks/epic?id=", ""));
                        try {
                            taskManager.deleteTaskById(id);
                            System.out.println("Эпик с id: " + id + " и его подзадачи удалены");
                            httpExchange.sendResponseHeaders(200,0);
                        } catch (NullPointerException e) {
                            throw new RuntimeException("Эпика с таким id не существует");
                        }
                    }else {
                        throw new RuntimeException("Путь или запрос неправильный");
                    }
                    break;

                case "POST":
                    InputStream is = httpExchange.getRequestBody();
                    String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(jsonString);
                    if(!jsonElement.isJsonObject()){
                        throw new RuntimeException("Неправильный обьект в теле запроса");
                    }else{
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Epic epic = gson.fromJson(jsonObject, Epic.class);
                        taskManager.createNewEpic(epic);
                    }
                    break;
                default:
                    throw new RuntimeException("Неправильный метод");
            }
        }
    }

    private class SubtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            switch (metod){
                case "GET":
                    if(path.startsWith("/tasks/subtask?id=")) {
                        int id = parseInt(path.replaceFirst("/tasks/subtask?id=", ""));
                        SubTask subTask = (SubTask)taskManager.getTaskMap().get(id);
                        String subTaskJson = gson.toJson(subTask, SubTask.class);
                        sendText(httpExchange, subTaskJson);
                    }else if(path.startsWith("/tasks/subtask/")) {          //TODO убрать или сделать
                        String allTasks = gson.toJson(taskManager.getTaskMap());
                        sendText(httpExchange, allTasks);
                    }else {
                        throw new RuntimeException("Путь или запрос неправильны");
                    }
                    break;
                case "DELETE":
                    if(Pattern.matches("^/tasks/subtask/?id=\\d+/", path)) {
                        int id = parseInt(path.replaceFirst("/tasks/subtask?id=", ""));
                        try {
                            taskManager.deleteTaskById(id);
                            System.out.println("Подзадача с id: " + id + " удалена");
                            httpExchange.sendResponseHeaders(200,0);
                        } catch (NullPointerException e) {
                            throw new RuntimeException("Подзадачи с таким id не существует");
                        }
                    }else {
                        throw new RuntimeException("Путь или запрос неправильный");
                    }
                    break;

                case "POST":
                    InputStream is = httpExchange.getRequestBody();
                    String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(jsonString);
                    if(!jsonElement.isJsonObject()){
                        throw new RuntimeException("Неправильный обьект в теле запроса");
                    }else{
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        SubTask subTask = gson.fromJson(jsonObject, SubTask.class);
                        taskManager.createNewSubtask(subTask);
                    }
                    break;
                default:
                    throw new RuntimeException("Неправильный метод");
            }
        }
    }

    private class PrioritizedTasksHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();

            switch (metod){
                case "GET":
                    if(Pattern.matches("^/tasks/$",path)){
                        TreeSet<Task> tasksSet = taskManager.getPrioritizedTasks();
                        sendText(httpExchange, gson.toJson(tasksSet));
                    } else {
                        throw new RuntimeException("Неверный путь");
                    }
            }
        }
    }

    private class HistoryHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            taskManager.getInMemoryHistoryManager();
        }
    }

    private int parseInt(String numberString){
        try {
            int number = Integer.parseInt(numberString);
            return number;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
    }
}

