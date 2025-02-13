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

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode(), "Ошибка при получении задач");

        List<Task> tasks = gson.fromJson(getResponse.body(), new TypeToken<ArrayList<Task>>() {}.getType());
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasks.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {

        Task task = new Task("Test 1", "Testing task 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.setTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI postUrl = URI.create("http://localhost:8080/tasks");
        String jsonRequest = gson.toJson(task);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(postUrl)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, postResponse.statusCode(), "Ошибка при добавлении задачи");

        URI getUrl = URI.create("http://localhost:8080/tasks");
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(getUrl)
                .GET()
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при получении задач");

        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());

        assertNotNull(tasks, "Задачи не возвращаются");
        assertFalse(tasks.isEmpty(), "Список задач пуст");

        assertEquals(1, tasks.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasks.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.setTask(task);

        List<Task> tasksBeforeDelete = manager.getAllTasks();
        assertEquals(1, tasksBeforeDelete.size(), "Задача не добавлена");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при удалении задачи");

        List<Task> tasksAfterDelete = manager.getAllTasks();
        assertTrue(tasksAfterDelete.isEmpty(), "Задача не удалена");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.setTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task retrievedTask = gson.fromJson(response.body(), Task.class); // Десериализуем объект, а не список
        assertEquals("Test 1", retrievedTask.getName());
    }

    @Test
    public void testGetNonExistentTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=999"); // ID, которого нет
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Задача должна быть не найдена для удаления");
    }
}
