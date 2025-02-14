package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import service.TaskManager;
import model.task.Task;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        System.out.println("[TaskHandler] Method: " + method + ", Path: " + path + ", Query: " + query);

        if (!"/tasks".equals(path)) {
            sendNotFound(exchange);
            return;
        }

        switch (method) {
            case "GET":
                if (query != null && query.contains("id=")) {
                    handleGetTaskById(exchange);
                } else {
                    handleGetTasks(exchange);
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

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        System.out.println("[handleGetTaskById] Query: " + query);
        if (query == null || !query.contains("id=")) {
            sendText(exchange, "Task ID is required", 404);
            return;
        }

        String[] params = query.split("&");
        int taskId = -1;
        for (String param : params) {
            if (param.startsWith("id=")) {
                try {
                    taskId = Integer.parseInt(param.split("=")[1]);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("[handleGetTaskById] Invalid task ID format: " + param);
                    sendText(exchange, "Invalid task ID", 400);
                    return;
                }
            }
        }

        if (taskId == -1) {
            sendText(exchange, "Task ID not found", 400);
            return;
        }

        Task task = taskManager.getTaskById(taskId);
        System.out.println("[handleGetTaskById] Task retrieved: " + task);

        if (task != null) {
            sendJsonResponse(exchange, task, 200);
        } else {
            sendText(exchange, "Task not found", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        System.out.println("[handleGetTasks] Retrieving all tasks.");
        sendJsonResponse(exchange, taskManager.getAllTasks(), 200);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        System.out.println("[handleCreateTask] Creating a new task.");
        try (InputStream inputStream = exchange.getRequestBody()) {
            String body = new String(inputStream.readAllBytes());
            System.out.println("[handleCreateTask] Request body: " + body);
            Task task = gson.fromJson(body, Task.class);
            if (task == null || task.getName() == null || task.getDescription() == null) {
                System.out.println("[handleCreateTask] Invalid task data.");
                sendText(exchange, "Invalid task data", 400);
                return;
            }
            taskManager.setTask(task);
            System.out.println("[handleCreateTask] Task created: " + task);
            sendSuccess(exchange);
        } catch (Exception e) {
            System.out.println("[handleCreateTask] Exception: " + e.getMessage());
            sendText(exchange, "Error creating task", 500);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        System.out.println("[handleDeleteTask] Query: " + query);
        if (query == null || !query.contains("id=")) {
            sendText(exchange, "Task ID is required", 400);
            return;
        }

        int taskId;
        try {
            taskId = Integer.parseInt(query.split("=")[1]);
        } catch (NumberFormatException e) {
            System.out.println("[handleDeleteTask] Invalid task ID format in query: " + query);
            sendText(exchange, "Invalid task ID", 400);
            return;
        }

        boolean removed = taskManager.removeTaskById(taskId);
        System.out.println("[handleDeleteTask] Task deletion for ID " + taskId + " result: " + removed);
        if (!removed) {
            sendText(exchange, "Task not found", 404);
        } else {
            sendSuccess(exchange);
        }
    }
}
