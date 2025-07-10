package handlers;

import adapters.SimpleTaskListTypeToken;
import model.EpicTask;
import model.PartEpicTask;
import model.SimpleTask;
import org.junit.jupiter.api.Test;
import util.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest extends BaseTaskClassTest {

    public HistoryHandlerTest() throws IOException {
    }

    @Test
    public void shouldReturnTrueIfReceiveCorrectPartEpicTasks() throws IOException, InterruptedException {
        //Given

        SimpleTask sTask1 = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        SimpleTask sTask2 = new SimpleTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));

        EpicTask epicTask = new EpicTask("name", "name");

        PartEpicTask task1 = new PartEpicTask(100504, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(5), epicTask.getId());
        PartEpicTask task2 = new PartEpicTask(100505, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(40), Duration.ofMinutes(5), epicTask.getId());

        manager.makeNewSimpleTask(sTask1);
        manager.makeNewEpicTask(epicTask);
        manager.makeNewPartEpicTask(task1);
        manager.updateSimpleTask(sTask2);
        manager.updatePartEpicTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SimpleTask> responseList = gson.fromJson(response.body(), new SimpleTaskListTypeToken().getType());

        //Then
        assertEquals(200, response.statusCode());
        assertNotNull(responseList, "Задачи не возвращаются");
        assertEquals(5, responseList.size(), "Некорректное количество задач");
        assertEquals(100502, responseList.get(3).getId(), "Некорректное количество задач");
    }
}