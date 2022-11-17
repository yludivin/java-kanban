package api;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.impl.FileBackedTasksManager;
import manager.interfaces.TaskManager;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private Gson gson;
    private HttpServer server;
    private String path = "resources\\HttpTaskServer.csv";
    private FileBackedTasksManager fileBackedTasksManager;
    private TaskManager taskManager;
    private static final int PORT = 8080;


    public HttpTaskServer() throws IOException {
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        //fileBackedTasksManager = new FileBackedTasksManager(path).loadFromFile(path);
        fileBackedTasksManager = Managers.getFileBackedManagerByPath(path).
                loadFromFile(Path.of("D:\\yp\\java-kanban\\resourses\\HttpTaskServer.csv"));
        taskManager = fileBackedTasksManager;
    }


    public void start() throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        System.out.println("HTTP-сервер запущен на порту: " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks/");
        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtask", new SubtaskHandler());
        server.createContext("/tasks", new PrioritizedTasksHandler());
        server.createContext("/tasks/history", new HistoryHandler());
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен на порту: " + PORT);
    }


    private class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String uri = httpExchange.getRequestURI().toString();
            switch (metod) {
                case "GET":
                     if (Pattern.matches("^/tasks/task\\?id=\\d+", uri)) {
                        int id = parseInt(uri.replaceFirst("/tasks/task\\?id=", ""));
                        Task task = taskManager.getTaskById(id);
                        String taskJson = gson.toJson(task, Task.class);
                        sendText(httpExchange, taskJson);
                    } else if (uri.startsWith("/tasks/task")) {
                        String allTasks = gson.toJson(taskManager.getOnlyTasks());
                        sendText(httpExchange, allTasks);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        System.out.println("Путь или запрос неправильный");
                    }
                    httpExchange.close();
                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/task\\?id=\\d+", uri)) {
                        int id = parseInt(uri.replaceFirst("/tasks/task\\?id=", ""));
                        boolean isDeleted = taskManager.deleteTaskById(id);
                        if (isDeleted) {
                            System.out.println("Задача с id " + id + "удалена");
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            System.out.println("Задачи с таким id не существует");
                        }
                    } else if (Pattern.matches("^/tasks/task$", uri)) {
                        taskManager.deleteOnlyTasks();
                        System.out.println("Все задачи удалены");
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        System.out.println("Путь или запрос неправильный");
                    }
                    httpExchange.close();
                    break;

                case "POST":
                    if (Pattern.matches("^/tasks/task$", uri)) {
                        InputStream is = httpExchange.getRequestBody();
                        String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            httpExchange.sendResponseHeaders(400, 0);
                            System.out.println("Неправильный обьект в теле запроса");
                        } else {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String name = jsonObject.get("name").getAsString();
                            String description = jsonObject.get("description").getAsString();
                            String startTime = jsonObject.get("startTime").getAsString();
                            long duration = Long.parseLong(jsonObject.get("duration").getAsString());
                            Task task = new Task(name, description, startTime,duration);
                            taskManager.createNewTask(task);
                            sendText(httpExchange,"Новая задача создана");
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    httpExchange.close();
                    break;

                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    System.out.println("Запрос неправильный");
                    httpExchange.close();
            }
        }
    }

    private class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String uri = httpExchange.getRequestURI().toString();
            switch (metod) {
                case "GET":
                    if (Pattern.matches("^/tasks/epic\\?id=\\d+", uri)) {
                        int id = parseInt(uri.replaceFirst("/tasks/epic\\?id=", ""));
                        Epic epic = (Epic)taskManager.getTaskById(id);
                        String epicJson = gson.toJson(epic, Epic.class);
                        sendText(httpExchange, epicJson);
                    } else if (uri.startsWith("/tasks/epic")) {
                        String allEpics = gson.toJson(taskManager.getOnlyEpics());
                        sendText(httpExchange, allEpics);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        System.out.println("Путь или запрос неправильный");
                    }
                    httpExchange.close();
                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/epic\\?id=\\d+", uri)) {
                        int id = parseInt(uri.replaceFirst("/tasks/epic\\?id=", ""));
                        boolean isDeleted = taskManager.deleteTaskById(id);
                        if (isDeleted) {
                            System.out.println("Задача с id " + id + "удалена");
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            System.out.println("Задачи с таким id не существует");
                        }
                    } else if (Pattern.matches("^/tasks/epic$", uri)) {
                        taskManager.deleteOnlyEpics();
                        System.out.println("Все эпика с подзадачами удалены");
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        System.out.println("Путь или запрос неправильный");
                    }
                    httpExchange.close();
                    break;

                case "POST":
                    if (Pattern.matches("^/tasks/epic$", uri)) {
                        InputStream is = httpExchange.getRequestBody();
                        String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            httpExchange.sendResponseHeaders(400, 0);
                            System.out.println("Неправильный обьект в теле запроса");
                        } else {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String name = jsonObject.get("name").getAsString();
                            String description = jsonObject.get("description").getAsString();
                            Epic epic = new Epic(name, description);
                            taskManager.createNewTask(epic);
                            sendText(httpExchange,"Новый эпик создан");
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    httpExchange.close();
                    break;

                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    System.out.println("Запрос неправильный");
                    httpExchange.close();
            }
        }
    }

    private class SubtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String uri = httpExchange.getRequestURI().toString();
            switch (metod) {
                case "GET":
                    if (Pattern.matches("^/tasks/subtask\\?id=\\d+", uri)) {
                        int id = parseInt(uri.replaceFirst("/tasks/subtask\\?id=", ""));
                        SubTask subtask = (SubTask) taskManager.getTaskById(id);
                        String subtaskJson = gson.toJson(subtask, SubTask.class);
                        sendText(httpExchange, subtaskJson);
                    } else if (uri.startsWith("/tasks/subtask")) {
                        String allTasks = gson.toJson(taskManager.getOnlySubtasks());
                        sendText(httpExchange, allTasks);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        System.out.println("Путь или запрос неправильный");
                    }
                    httpExchange.close();
                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/subtask\\?id=\\d+", uri)) {
                        int id = parseInt(uri.replaceFirst("/tasks/subtask\\?id=", ""));
                        boolean isDeleted = taskManager.deleteTaskById(id);
                        if (isDeleted) {
                            System.out.println("Подзадача с id " + id + "удалена");
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            System.out.println("Подзадачи с таким id не существует");
                        }
                    } else if (Pattern.matches("^/tasks/subtask$", uri)) {
                        taskManager.deleteOnlySubtasks();
                        System.out.println("Все подзадачи удалены, эпики очищены");
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        System.out.println("Путь или запрос неправильный");
                    }
                    httpExchange.close();
                    break;

                case "POST":
                    if (Pattern.matches("^/tasks/subtask$", uri)) {
                        InputStream is = httpExchange.getRequestBody();
                        String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            httpExchange.sendResponseHeaders(400, 0);
                            System.out.println("Неправильный обьект в теле запроса");
                        } else {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String name = jsonObject.get("name").getAsString();
                            String description = jsonObject.get("description").getAsString();
                            String startTime = jsonObject.get("startTime").getAsString();
                            long duration = Long.parseLong(jsonObject.get("duration").getAsString());
                            int epicId = Integer.parseInt(jsonObject.get("epicId").getAsString());
                            SubTask subTask = new SubTask(name, description, startTime,duration,
                                    (Epic)taskManager.getTaskById(epicId));
                            taskManager.createNewTask(subTask);
                            sendText(httpExchange,"Новая задача создана");
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    httpExchange.close();
                    break;

                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    System.out.println("Запрос неправильный");
                    httpExchange.close();
            }
        }
    }

    private class PrioritizedTasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();

            switch (metod) {
                case "GET":
                    if (Pattern.matches("^/tasks$", path)) {
                        TreeSet<Task> tasksSet = taskManager.getPrioritizedTasks();
                        sendText(httpExchange, gson.toJson(tasksSet));
                    } else {
                        throw new RuntimeException("Неверный путь");
                    }
            }
        }
    }

    private class HistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            switch (metod) {
                case "GET":
                    if (Pattern.matches("^/tasks/history$", path)) {
                        String response = gson.toJson(taskManager.getInMemoryHistoryManager().getHistory());
                        sendText(httpExchange, response);
                    } else {
                        throw new RuntimeException("Неверный путь");
                    }
                    break;
                default:
                    throw new RuntimeException("Неправильный метод");
            }

        }
    }

    private int parseInt(String numberString) {
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
        exchange.close();
    }
}

