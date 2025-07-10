package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.SimpleTask;
import service.task.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseTasksHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void getTasks(HttpExchange httpExchange, Integer id) throws IOException {
        String response;

        if (id == null) {
            List<SimpleTask> taskList = taskManager.getAllSimpleTask();
            response = gson.toJson(taskList);
        } else {
            SimpleTask task = taskManager.getSimpleTaskById(id);
            response = gson.toJson(task);
        }

        sendText(httpExchange, response);
    }

    @Override
    protected void createTask(HttpExchange httpExchange, Integer id) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SimpleTask task = gson.fromJson(body, SimpleTask.class);

        if (id == null) {
            taskManager.makeNewSimpleTask(task);
        } else {
            taskManager.updateSimpleTask(task);
        }

        sendPostText(httpExchange, body);
    }

    @Override
    protected void deleteTask(HttpExchange httpExchange, Integer id) throws IOException {
        if (id == null) {
            throw new NotFoundException();
        } else {
            SimpleTask task = taskManager.getSimpleTaskById(id);
            String responseBody = gson.toJson(task);
            taskManager.removeSimpleTaskById(id);
            sendText(httpExchange, responseBody);
        }
    }

}
