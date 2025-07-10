package server;

import com.sun.net.httpserver.HttpServer;
import handlers.*;
import service.task.TaskManager;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.taskManager = Managers.getTaskManager();
    }

    public void start() {
        initializeServer();
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    private void initializeServer() {
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }
}
