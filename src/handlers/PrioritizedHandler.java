package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.ScheduleConflictException;
import model.SimpleTask;
import service.task.TaskManager;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                Set<SimpleTask> simpleTaskList = taskManager.getPrioritizedTasks();
                String response = gson.toJson(simpleTaskList);
                sendText(httpExchange, response);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        } catch (ScheduleConflictException e) {
            sendHasOverlaps(httpExchange);
        }
    }
}
