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

class EpicsHandlerTest extends BaseTaskClassTest {

    EpicsHandlerTest() throws IOException {
    }

    @Test
    public void shouldReturnTrueIfReceiveCorrectEpicTasks() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        manager.makeNewEpicTask(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
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
        assertEquals(1, responseList.size(), "Некорректное количество задач");
        assertEquals("name", responseList.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldReturnTrueIfReceiveCorrectPartEpicTaskById() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        manager.makeNewEpicTask(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicTask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        EpicTask responseTask = gson.fromJson(response.body(), EpicTask.class);

        //Then
        assertEquals(200, response.statusCode());
        assertNotNull(responseTask, "Задачи не возвращаются");
        assertEquals(responseTask, epicTask);
    }

    @Test
    public void shouldReceiveStatus404WhenTryGetNonExistentIdEpicTask() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        manager.makeNewEpicTask(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicTask.getId() + 100);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldReturnTrueWhenPostCorrectEpicTask() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        manager.makeNewEpicTask(epicTask);

        String taskJson = gson.toJson(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<EpicTask> tasksFromManager = manager.getAllEpicTask();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldReturnTrueWhenPostCorrectEpicTaskById() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        String taskJson = gson.toJson(epicTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicTask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<EpicTask> tasksFromManager = manager.getAllEpicTask();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(epicTask.getId(), tasksFromManager.get(0).getId(), "Некорректное имя задачи");
    }

    @Test
    public void shouldReturnTrueWhenDeleteCorrectTask() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        PartEpicTask task1 = new PartEpicTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5), epicTask.getId());
        PartEpicTask task2 = new PartEpicTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5), epicTask.getId());
        PartEpicTask task3 = new PartEpicTask(100503, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(5), epicTask.getId());

        manager.makeNewEpicTask(epicTask);
        manager.makeNewPartEpicTask(task1);
        manager.makeNewPartEpicTask(task2);
        manager.makeNewPartEpicTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicTask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<EpicTask> epicList = manager.getAllEpicTask();
        List<PartEpicTask> subTaskList = manager.getAllPartEpicTask();

        assertEquals(200, response.statusCode());
        assertTrue(epicList.isEmpty(), "List must be empty");
        assertTrue(subTaskList.isEmpty(), "List must be empty");
    }
}