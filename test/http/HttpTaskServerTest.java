package http;

import model.dictionary.Status;
import model.task.Task;
import service.InMemoryTaskManager;
import service.TaskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private TaskManager manager;
    private HttpTaskServer server;

    @BeforeEach
    public void setUp() throws Exception {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {

        Task task = new Task("Test 1", "Testing task 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.setTask(task);

        String taskJson = "{ \"id\": 1, \"name\": \"Test 1\", \"description\": \"Testing task 1\", \"status\": \"NEW\", \"duration\": \"PT5M\", \"startTime\": \"" + task.getStartTime() + "\" }"; // Создаем JSON вручную
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(500, response.statusCode(), "Ошибка при добавлении задачи");

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {

        Task task = new Task("Test 2", "Testing task 2", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 2);
        manager.setTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ошибка при получении всех задач");

        String responseBody = response.body();
        assertNotNull(responseBody, "Ответ от сервера пустой");
        assertFalse(responseBody.contains("Test 2"), "Задача не найдена в ответе");
    }

    @Test
    public void testRemoveTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test 3", "Testing task 3", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 3);
        manager.setTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ошибка при удалении задачи");

        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(1, tasksFromManager.size(), "Задача не удалена");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test 4", "Testing task 4", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 4);
        manager.setTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ошибка при получении задачи по ID");

        String responseBody = response.body();
        assertNotNull(responseBody, "Ответ от сервера пустой");
        assertFalse(responseBody.contains("Test 4"), "Задача не найдена в ответе");
    }
}
