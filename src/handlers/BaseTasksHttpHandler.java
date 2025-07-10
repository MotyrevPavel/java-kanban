package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.ScheduleConflictException;
import service.task.TaskManager;

import java.io.IOException;

public abstract class BaseTasksHttpHandler extends BaseHttpHandler implements HttpHandler {

    public BaseTasksHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Integer id = getSecondParameterFromURI(httpExchange);
        String method = httpExchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    getTasks(httpExchange, id);
                    break;
                case "POST":
                    createTask(httpExchange, id);
                    break;
                case "DELETE":
                    deleteTask(httpExchange, id);
            }
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        } catch (ScheduleConflictException e) {
            sendHasOverlaps(httpExchange);
        }
    }

    protected abstract void getTasks(HttpExchange httpExchange, Integer id) throws IOException;

    protected abstract void createTask(HttpExchange httpExchange, Integer id) throws IOException;

    protected abstract void deleteTask(HttpExchange httpExchange, Integer id) throws IOException;

}
