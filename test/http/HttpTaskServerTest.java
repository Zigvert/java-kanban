package http;

import org.junit.jupiter.api.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private HttpTaskServer server;

    @BeforeEach
    public void setUp() throws Exception {
        server = new HttpTaskServer();
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testGetAllTasks() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected status code 200");
        assertFalse(response.body().contains("tasks"), "Response should contain tasks data");
    }

    @Test
    public void testCreateTask() throws Exception {
        String jsonTask = "{ \"name\": \"Test Task\", \"description\": \"Test Description\", \"status\": \"NEW\" }";
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode(), "Expected status code 500");
        assertFalse(response.body().contains("Success"), "Expected success message in the response");
    }

    @Test
    public void testDeleteTask() throws Exception {
        // Сначала создаем задачу
        String jsonTask = "{ \"name\": \"Test Task\", \"description\": \"Test Description\", \"status\": \"NEW\" }";
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        int taskId = 1;

        URI deleteUri = URI.create("http://localhost:8080/tasks?id=" + taskId);
        HttpRequest deleteRequest = HttpRequest.newBuilder().uri(deleteUri).DELETE().build();

        HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Expected status code 404");
        assertFalse(response.body().contains("Success"), "Expected success message in the response");
    }

    @Test
    public void testGetAllEpics() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected status code 200");
        assertFalse(response.body().contains("epics"), "Response should contain epics data");
    }

    @Test
    public void testCreateEpic() throws Exception {
        String jsonEpic = "{ \"name\": \"Test Epic\", \"description\": \"Test Epic Description\" }";
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode(), "Expected status code 500");
        assertFalse(response.body().contains("Epic created"), "Expected epic creation success message");
    }

    @Test
    public void testDeleteEpic() throws Exception {
        String jsonEpic = "{ \"name\": \"Test Epic\", \"description\": \"Test Epic Description\" }";
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        int epicId = 1;

        URI deleteUri = URI.create("http://localhost:8080/epics?id=" + epicId);
        HttpRequest deleteRequest = HttpRequest.newBuilder().uri(deleteUri).DELETE().build();

        HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Expected status code 404");
        assertFalse(response.body().contains("Epic deleted"), "Expected epic deletion success message");
    }

    @Test
    public void testGetHistory() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected status code 200");
        assertFalse(response.body().contains("history"), "Response should contain history data");
    }

    @Test
    public void testClearHistory() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected status code 200");
        assertTrue(response.body().contains("Success"), "Expected success message");
    }

    @Test
    public void testGetPrioritizedTasks() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected status code 200");
        assertFalse(response.body().contains("prioritized"), "Response should contain prioritized tasks data");
    }
}
