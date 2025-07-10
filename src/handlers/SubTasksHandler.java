package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.PartEpicTask;
import service.task.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubTasksHandler extends BaseTasksHttpHandler {

    public SubTasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void getTasks(HttpExchange httpExchange, Integer id) throws IOException {
        String response;

        if (id == null) {
            List<PartEpicTask> taskList = taskManager.getAllPartEpicTask();
            response = gson.toJson(taskList);
        } else {
            PartEpicTask task = taskManager.getPartEpicTaskById(id);
            response = gson.toJson(task);
        }

        sendText(httpExchange, response);
    }

    @Override
    protected void createTask(HttpExchange httpExchange, Integer id) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        PartEpicTask task = gson.fromJson(body, PartEpicTask.class);

        if (id == null) {
            taskManager.makeNewPartEpicTask(task);
        } else {
            taskManager.updatePartEpicTask(task);
        }

        sendPostText(httpExchange, body);
    }

    @Override
    protected void deleteTask(HttpExchange httpExchange, Integer id) throws IOException {
        if (id == null) {
            throw new NotFoundException();
        } else {
            PartEpicTask task = taskManager.getPartEpicTaskById(id);
            String responseBody = gson.toJson(task);
            taskManager.removePartEpicTaskById(id);
            sendText(httpExchange, responseBody);
        }
    }
}
