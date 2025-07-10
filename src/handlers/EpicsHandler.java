package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.EpicTask;
import model.PartEpicTask;
import service.task.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseTasksHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void getTasks(HttpExchange httpExchange, Integer id) throws IOException {
        String response;

        if (id == null) {
            List<EpicTask> taskList = taskManager.getAllEpicTask();
            response = gson.toJson(taskList);
        } else {
            String uriThirdParameter = getThirdParameterFromURI(httpExchange);
            if (uriThirdParameter == null) {
                EpicTask epicTask = taskManager.getEpicTaskById(id);
                response = gson.toJson(epicTask);
            } else if (uriThirdParameter.equals("subtasks")) {
                EpicTask epicTask = taskManager.getEpicTaskById(id);
                List<PartEpicTask> partEpicTaskList = taskManager.getEpicSubtasks(epicTask);
                response = gson.toJson(partEpicTaskList);
            } else {
                throw new NotFoundException();
            }
        }

        sendText(httpExchange, response);
    }

    @Override
    protected void createTask(HttpExchange httpExchange, Integer id) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        EpicTask task = gson.fromJson(body, EpicTask.class);

        if (id == null) {
            taskManager.makeNewEpicTask(task);
        } else {
            taskManager.updateEpicTask(task);
        }

        sendPostText(httpExchange, body);
    }

    @Override
    protected void deleteTask(HttpExchange httpExchange, Integer id) throws IOException {
        if (id == null) {
            throw new NotFoundException();
        } else {
            EpicTask task = taskManager.getEpicTaskById(id);
            String responseBody = gson.toJson(task);
            taskManager.removeEpicTaskById(id);
            sendText(httpExchange, responseBody);
        }
    }

}
