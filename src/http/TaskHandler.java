package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import service.TaskManager;
import service.ManagerProvider;
import model.task.Task;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager = ManagerProvider.getDefault();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                if (exchange.getRequestURI().getPath().equals("/tasks")) {
                    handleGetTasks(exchange);
                } else {
                    sendNotFound(exchange);
                }
                break;
            case "POST":
                handleCreateTask(exchange);
                break;
            case "DELETE":
                handleDeleteTask(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, taskManager.getAllTasks(), 200);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            Task task = gson.fromJson(new String(inputStream.readAllBytes()), Task.class);
            if (task == null || task.getName() == null || task.getDescription() == null) {
                sendText(exchange, "Invalid task data", 400);
                return;
            }
            taskManager.setTask(task);
            sendSuccess(exchange);
        } catch (Exception e) {
            sendText(exchange, "Error creating task", 500);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.startsWith("id=")) {
            sendNotFound(exchange);
            return;
        }

        int taskId = Integer.parseInt(query.split("=")[1]);
        boolean removed = taskManager.removeTaskById(taskId);
        if (!removed) {
            sendNotFound(exchange);
        } else {
            sendSuccess(exchange);
        }
    }
}
