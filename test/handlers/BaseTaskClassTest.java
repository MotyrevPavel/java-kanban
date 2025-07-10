package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import service.task.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseTaskClassTest {
    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager manager = taskServer.getTaskManager();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    BaseTaskClassTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllEpicTask();
        manager.removeAllSimpleTask();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }
}
