package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import service.ManagerProvider;
import model.task.Subtask;

import java.io.IOException;
import java.io.InputStream;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                if (exchange.getRequestURI().getPath().equals("/subtasks")) {
                    handleGetSubtasks(exchange);
                } else {
                    sendNotFound(exchange);
                }
                break;
            case "POST":
                handleCreateSubtask(exchange);
                break;
            case "DELETE":
                handleDeleteSubtask(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, taskManager.getAllSubtasks(), 200);
    }

    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            Subtask subtask = gson.fromJson(new String(inputStream.readAllBytes()), Subtask.class);
            if (subtask == null || subtask.getName() == null || subtask.getDescription() == null) {
                sendText(exchange, "Invalid subtask data", 400);
                return;
            }
            taskManager.addSubtask(subtask);
            sendSuccess(exchange);
        } catch (Exception e) {
            sendText(exchange, "Error creating subtask", 500);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.startsWith("id=")) {
            sendNotFound(exchange);
            return;
        }

        int subtaskId = Integer.parseInt(query.split("=")[1]);
        boolean removed = taskManager.removeSubtaskById(subtaskId);
        if (!removed) {
            sendNotFound(exchange);
        } else {
            sendSuccess(exchange);
        }
    }
}
