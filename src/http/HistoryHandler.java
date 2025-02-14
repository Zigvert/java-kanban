package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.HistoryManager;


import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final HistoryManager historyManager;

    public HistoryHandler(HistoryManager historyManager) {
        this.historyManager = historyManager;
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
        sendJsonResponse(exchange, historyManager.getHistoryTask(), 200);
    }

}
