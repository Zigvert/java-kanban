import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Epic epic = taskManager.createEpic(new Epic("Организация мероприятия", "Организовать встречу"));
        Subtask subtask1 = taskManager.createSubtask(new Subtask("Выбрать место", "Найти подходящее место", epic.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("Пригласить гостей", "Разослать приглашения", epic.getId()));

        System.out.println("Подзадачи эпика:");
        System.out.println(taskManager.getSubtasksByEpicId(epic.getId()));

        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateEpicStatus(epic);
        System.out.println("Статус эпика после обновления: " + epic.getStatus());

        taskManager.deleteAllSubtasks();
        System.out.println("Подзадачи после удаления: " + taskManager.getAllSubtasks());
    }
}
