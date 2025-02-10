package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.HistoryManager;
import service.ManagerProvider;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private HistoryManager historyManager = ManagerProvider.getDefaultHistory();

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
            case "DELETE":
                handleClearHistory(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, historyManager.getHistoryTask(), 200);
    }

    private void handleClearHistory(HttpExchange exchange) throws IOException {
        historyManager.clearHistory();
        sendSuccess(exchange);
    }
}
