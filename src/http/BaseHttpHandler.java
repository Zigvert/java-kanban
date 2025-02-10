package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseHttpHandler {
    protected Gson gson = new Gson();

    protected void sendText(HttpExchange exchange, String text, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, text.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "Resource not found", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "Task conflict", 406);
    }

    protected void sendSuccess(HttpExchange exchange) throws IOException {
        sendText(exchange, "Success", 200);
    }

    // Метод для отправки JSON-ответа
    protected void sendJsonResponse(HttpExchange exchange, Object responseObject, int responseCode) throws IOException {
        try {
            String jsonResponse = gson.toJson(responseObject);
            sendText(exchange, jsonResponse, responseCode);
        } catch (Exception e) {
            sendText(exchange, "Failed to serialize response", 500);
        }
    }
}
