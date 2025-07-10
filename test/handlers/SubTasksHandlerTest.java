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

class SubTasksHandlerTest extends BaseTaskClassTest {

    SubTasksHandlerTest() throws IOException {
    }

    @Test
    public void shouldReturnTrueIfReceiveCorrectPartEpicTasks() throws IOException, InterruptedException {
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
        URI url = URI.create("http://localhost:8080/subtasks");
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
        assertEquals(3, responseList.size(), "Некорректное количество задач");
    }

    @Test
    public void shouldReturnTrueIfReceiveCorrectPartEpicTaskById() throws IOException, InterruptedException {
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
        URI url = URI.create("http://localhost:8080/subtasks/100502");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        PartEpicTask responseTask = gson.fromJson(response.body(), PartEpicTask.class);

        //Then
        assertEquals(200, response.statusCode());
        assertNotNull(responseTask, "Задачи не возвращаются");
        assertEquals(responseTask, task2);
    }

    @Test
    public void shouldReceiveStatus404WhenTryGetNonExistentIdPartEpicTask() throws IOException, InterruptedException {
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
        URI url = URI.create("http://localhost:8080/subtasks/100504");
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
    public void shouldReturnTrueWhenPostCorrectPartEpicTask() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        PartEpicTask task1 = new PartEpicTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5), epicTask.getId());

        manager.makeNewEpicTask(epicTask);

        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<PartEpicTask> tasksFromManager = manager.getAllPartEpicTask();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldThrowExceptionWhenPostPartTaskWithTimeOverlap() throws IOException, InterruptedException {
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

        PartEpicTask task = new PartEpicTask(100504, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30), epicTask.getId());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<PartEpicTask> tasksFromManager = manager.getAllPartEpicTask();

        assertEquals(406, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");
    }


    @Test
    public void shouldReturnTrueWhenPostCorrectPartEpicTaskById() throws IOException, InterruptedException {
        //Given
        EpicTask epicTask = new EpicTask("name", "name");

        manager.makeNewEpicTask(epicTask);

        PartEpicTask task = new PartEpicTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5), epicTask.getId());

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/100501");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<PartEpicTask> tasksFromManager = manager.getAllPartEpicTask();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(100501, tasksFromManager.get(0).getId(), "Некорректное имя задачи");
    }


    @Test
    public void shouldThrowExceptionWhenPostSimpleTaskByIdWithTimeOverlap() throws IOException, InterruptedException {
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

        PartEpicTask task = new PartEpicTask(100504, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30), epicTask.getId());

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/100504");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<PartEpicTask> tasksFromManager = manager.getAllPartEpicTask();

        assertEquals(406, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");
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
        URI url = URI.create("http://localhost:8080/subtasks/100502");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<PartEpicTask> taskList = manager.getAllPartEpicTask();
        assertEquals(200, response.statusCode());
        assertEquals(2, taskList.size(), "Некорректное количество задач");
    }
}