package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import service.ManagerProvider;
import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager = ManagerProvider.getDefault();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET") && exchange.getRequestURI().getPath().equals("/prioritized")) {
            handleGetPrioritizedTasks(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, taskManager.getPrioritizedTasks(), 200);
    }
}
