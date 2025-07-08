package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.ScheduleConflictException;
import model.SimpleTask;
import service.task.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange httpExchange) {

        Integer id = getIdFromURI(httpExchange);
        String method = httpExchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    getTasks(httpExchange, id);
                    break;
                case "POST" -> createTask(httpExchange, id);
                break;
                case "DELETE" -> deleteTask(httpExchange, id);
                break;
            }
        } catch (NotFoundException ex) {

        } catch (IOException ex) {

        }
    }

    private Integer getIdFromURI(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        boolean isPathHasId = splitPath.length == 3;
        return isPathHasId ? Integer.parseInt(splitPath[2]) : null;
    }

    private void getTasks(HttpExchange httpExchange, Integer id) throws IOException {
        String response;

        if (id == null) {
            List<SimpleTask> taskList = taskManager.getAllSimpleTask();
            response = gson.toJson(taskList);
        } else {
            SimpleTask task = taskManager.getSimpleTaskById(id);
            response = gson.toJson(task);
        }

    }

    private String getTaskById(HttpExchange httpExchange, Integer id) throws IOException {
        SimpleTask task = taskManager.getSimpleTaskById(id);
        if (task == null) {
            httpExchange.sendResponseHeaders(404, 0);
            return "";
        } else {
            httpExchange.sendResponseHeaders(200, 0);
            return gson.toJson(task);
        }
    }

    private boolean createTask(HttpExchange httpExchange) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SimpleTask task = gson.fromJson(body, SimpleTask.class);
        try {
            taskManager.makeNewSimpleTask(task);
            httpExchange.sendResponseHeaders(201, 0);
            return true;
        } catch (ScheduleConflictException e) {
            httpExchange.sendResponseHeaders(406, 0);
            return false;
        }
    }

    private String updateTask() {

    }

    private boolean deleteTask(Integer id) {

    }

    // sendText для отправки общего ответа в случае успеха;
    // sendNotFound для отправки ответа в случае, если объект не был найден;
    // sendHasOverlaps для отправки ответа, если при создании или обновлении задача пересекается с уже существующими.

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.sendResponseHeaders(404, 0);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }



    /*
     * Код можно сделать ещё лаконичнее, пробрасывая NotFoundException в TaskManager.
     * Тогда в обработчиках не нужно проверять экземпляр Task на null — можно обрабатывать сразу исключение.
     * Реализуйте такой подход. Также добавьте try — catch — он будет обрабатывать все исключения,
     * которые возникают во время работы программы.
     * */
}
