import tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        // Создание эпика
        Epic epic = taskManager.createEpic(new Epic("Организация мероприятия", "Организовать встречу"));
        // Создание подзадач
        Subtask subtask1 = taskManager.createSubtask(new Subtask("Выбрать место", "Найти подходящее место", epic.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("Пригласить гостей", "Разослать приглашения", epic.getId()));

        System.out.println("Подзадачи эпика:");
        System.out.println(taskManager.getSubtasksByEpicId(epic.getId()));

        // Обновление статуса подзадачи
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);

        // Обновление статуса эпика
        System.out.println("Статус эпика после обновления: " + epic.getStatus());

        // Удаление подзадачи
        taskManager.deleteSubtaskById(subtask2.getId());
        System.out.println("Подзадачи после удаления одной: " + taskManager.getSubtasksByEpicId(epic.getId()));

        // Печать истории просмотров
        System.out.println("История просмотров:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }
}
