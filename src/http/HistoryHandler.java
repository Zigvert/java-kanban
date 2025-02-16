package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                if (exchange.getRequestURI().getPath().equals("/history")) {
                    handleGetHistory(exchange);
                } else {
                    sendNotFound(exchange);
                }
                break;
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, taskManager.getHistory(), 200);
    }

}
