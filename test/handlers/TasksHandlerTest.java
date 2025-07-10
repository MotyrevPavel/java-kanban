package handlers;

import adapters.SimpleTaskListTypeToken;
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

class TasksHandlerTest extends BaseTaskClassTest {

    TasksHandlerTest() throws IOException {
    }

    @Test
    public void shouldReturnTrueIfReceiveCorrectSimpleTasks() throws IOException, InterruptedException {
        //Given
        SimpleTask task1 = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        SimpleTask task2 = new SimpleTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        SimpleTask task3 = new SimpleTask(100503, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(5));

        manager.makeNewSimpleTask(task1);
        manager.makeNewSimpleTask(task2);
        manager.makeNewSimpleTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
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
    public void shouldReturnTrueIfReceiveCorrectSimpleTaskById() throws IOException, InterruptedException {
        //Given
        SimpleTask task1 = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        SimpleTask task2 = new SimpleTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        SimpleTask task3 = new SimpleTask(100503, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(5));

        manager.makeNewSimpleTask(task1);
        manager.makeNewSimpleTask(task2);
        manager.makeNewSimpleTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/100502");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SimpleTask responseTask = gson.fromJson(response.body(), SimpleTask.class);

        //Then
        assertEquals(200, response.statusCode());
        assertNotNull(responseTask, "Задачи не возвращаются");
        assertEquals(responseTask, task2);
    }

    @Test
    public void shouldReceiveStatus404WhenTryGetNonExistentId() throws IOException, InterruptedException {
        //Given
        SimpleTask task1 = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        SimpleTask task2 = new SimpleTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        SimpleTask task3 = new SimpleTask(100503, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(5));

        manager.makeNewSimpleTask(task1);
        manager.makeNewSimpleTask(task2);
        manager.makeNewSimpleTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/100504");
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
    public void shouldReturnTrueWhenPostCorrectSimpleTask() throws IOException, InterruptedException {
        //Given
        SimpleTask task = new SimpleTask("Test", "Test", LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<SimpleTask> tasksFromManager = manager.getAllSimpleTask();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldThrowExceptionWhenPostSimpleTaskWithTimeOverlap() throws IOException, InterruptedException {
        //Given
        SimpleTask task1 = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        SimpleTask task2 = new SimpleTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        SimpleTask task3 = new SimpleTask(100503, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(5));

        manager.makeNewSimpleTask(task1);
        manager.makeNewSimpleTask(task2);
        manager.makeNewSimpleTask(task3);

        SimpleTask task = new SimpleTask(100504, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<SimpleTask> tasksFromManager = manager.getAllSimpleTask();

        assertEquals(406, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void shouldReturnTrueWhenPostCorrectSimpleTaskById() throws IOException, InterruptedException {
        //Given
        SimpleTask task = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/100501");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<SimpleTask> tasksFromManager = manager.getAllSimpleTask();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(100501, tasksFromManager.get(0).getId(), "Некорректное имя задачи");
    }

    @Test
    public void shouldThrowExceptionWhenPostSimpleTaskByIdWithTimeOverlap() throws IOException, InterruptedException {
        //Given
        SimpleTask task1 = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        SimpleTask task2 = new SimpleTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        SimpleTask task3 = new SimpleTask(100503, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(5));

        manager.makeNewSimpleTask(task1);
        manager.makeNewSimpleTask(task2);
        manager.makeNewSimpleTask(task3);

        SimpleTask task = new SimpleTask(100504, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/100504");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<SimpleTask> tasksFromManager = manager.getAllSimpleTask();

        assertEquals(406, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void shouldReturnTrueWhenDeleteCorrectTask() throws IOException, InterruptedException {
        //Given
        SimpleTask task1 = new SimpleTask(100501, "Test1", "Test1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        SimpleTask task2 = new SimpleTask(100502, "Test2", "Test2",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        SimpleTask task3 = new SimpleTask(100503, "Test3", "Test3",
                TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(5));

        manager.makeNewSimpleTask(task1);
        manager.makeNewSimpleTask(task2);
        manager.makeNewSimpleTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/100502");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Then
        List<SimpleTask> simpleTasksList = manager.getAllSimpleTask();
        assertEquals(200, response.statusCode());
        assertEquals(2, simpleTasksList.size(), "Некорректное количество задач");
    }
}