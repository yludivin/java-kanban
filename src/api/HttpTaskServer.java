package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
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
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println("HTTP-сервер запущен на порту: " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks/");
    }

    public void stop(){
        server.stop(0);
        System.out.println("HTTP-сервер остановлен на порту: " + PORT);
    }


    private class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String metod = httpExchange.getRequestBody().toString();
            String path = httpExchange.getRequestURI().getPath();
            switch (metod){
                case "GET":
                    if(path.equals("/tasks/")){                                                             //1
                        taskManager.getPrioritizedTasks();
                    }else if(Pattern.matches("^/tasks/history/", path)){                              //2
                            //TODO  getHistory()

                    }else if(Pattern.matches("^/tasks/task/", path)){                                 //3
                            taskManager.getTasks();
                    }else if(Pattern.matches("^/tasks/task/?id=\\d+/", path)){                        //4
                        String number = path.replaceFirst("/tasks/task/?id=", "");
                        int id = parseInt(number);
                        taskManager.getTaskById(id);
                    } else if (Pattern.matches("^/tasks/subtask/epic/?id=", path)) {                  //5
                        String number = path.replaceFirst("/tasks/subtask/epic/?id=", "");
                        int id = parseInt(number);
                        //Epic epic = (Epic)taskManager.getTaskById(id);  //TODO вернуть эпик по этому
                    }else {
                        //неправильный запрос метода
                    }
                    break;
                case "DELETE":
                    if(Pattern.matches("^/tasks/task/?id=\\d+/", path)) {                             //6
                        String number = path.replaceFirst("/tasks/task/?id=", "");
                        int id = parseInt(number);
                        taskManager.deleteTaskById(id);
                    } else if (Pattern.matches("^/tasks/task/", path)) {                              //7
                        taskManager.deleteAllTasks();
                    }
                    break;
                case "POST":                                                                                //8
                    String body = httpExchange.getRequestBody().toString();

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
}
