package http;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
import model.task.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                handleGetEpic(exchange);
                break;
            case "POST":
                handleCreateOrUpdateEpic(exchange);
                break;
            case "DELETE":
                handleDeleteEpic(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getAllEpics());
        sendJsonResponse(exchange, response, 200);
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            Epic epic = gson.fromJson(new String(inputStream.readAllBytes()), Epic.class);

            if (epic == null || epic.getName() == null || epic.getDescription() == null) {
                sendText(exchange, "Invalid epic data", 400);
                return;
            }

            if (epic.getId() == 0) {
                manager.setTask(epic);
                sendResponse(exchange, "Epic created", 201);
            } else {
                manager.updateTask(epic);
                sendResponse(exchange, "Epic updated", 200);
            }
        } catch (Exception e) {
            sendText(exchange, "Error processing epic", 500);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.startsWith("id=")) {
            sendNotFound(exchange);
            return;
        }

        int epicId = Integer.parseInt(query.split("=")[1]);
        boolean removed = manager.removeTaskById(epicId);
        if (!removed) {
            sendNotFound(exchange);
        } else {
            sendResponse(exchange, "Epic deleted", 200);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "Resource not found", 404);
    }
}
