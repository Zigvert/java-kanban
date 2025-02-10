package http;

import com.sun.net.httpserver.HttpServer;
import service.TaskManager;
import service.ManagerProvider;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private HttpServer server;

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }

    public void start() throws IOException {
        TaskManager taskManager = ManagerProvider.getDefault();

        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    public void stop() {
        server.stop(0);
    }
}
