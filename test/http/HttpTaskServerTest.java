package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.dictionary.Status;
import model.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager manager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new BaseHttpHandler.LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new BaseHttpHandler.DurationAdapter())
            .create();

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при добавлении задачи");

        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now(), 1);

        manager.setTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении задач");

        List<Task> tasksFromResponse = gson.fromJson(response.body(),
                new TypeToken<ArrayList<Task>>() {}.getType());
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromResponse, "Задачи не возвращаются");
        assertEquals(tasksFromManager.size(), tasksFromResponse.size(),
                "Некорректное количество задач");
        if (!tasksFromResponse.isEmpty()) {
            assertEquals("Test 1", tasksFromResponse.get(0).getName(),
                    "Некорректное имя задачи");
        }
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.setTask(task);

        List<Task> tasksBeforeDelete = manager.getAllTasks();
        assertEquals(1, tasksBeforeDelete.size(), "Задача не добавлена");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при удалении задачи");

        List<Task> tasksAfterDelete = manager.getAllTasks();
        assertTrue(tasksAfterDelete.isEmpty(), "Задача не удалена");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.setTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(),
                "Ошибка при получении задачи по id");

        Task retrievedTask = gson.fromJson(response.body(), Task.class);
        List<Task> tasksFromManager = manager.getAllTasks();
        assertFalse(tasksFromManager.isEmpty(), "Задача не найдена в менеджере");
        assertEquals("Test 1", retrievedTask.getName(),
                "Некорректное имя задачи");
    }

    @Test
    public void testGetNonExistentTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Задача должна быть не найдена");
    }

    @Test
    public void testDeleteNonExistentTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(),
                "Задача должна быть не найдена для удаления");
    }
}
