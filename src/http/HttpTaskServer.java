package http;

import com.sun.net.httpserver.HttpServer;
import service.HistoryManager;
import service.TaskManager;
import service.ManagerProvider;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private HttpServer server;
    private TaskManager taskManager;
    private HistoryManager historyManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public HttpTaskServer(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer(ManagerProvider.getDefault());
        taskServer.start();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(historyManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    public void stop() {
        server.stop(0);
    }
}
