public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание задач
        Task task1 = taskManager.createTask("Переезд", "Переезд на новую квартиру");
        Task task2 = taskManager.createTask("Покупка автомобиля", "Купить новый автомобиль");

        // Создание эпиков и подзадач
        Epic epic1 = taskManager.createEpic("Организация праздника", "Организация семейного праздника");
        Subtask subtask1 = taskManager.createSubtask("Пригласить гостей", "Пригласить всех родственников", epic1.getId());
        Subtask subtask2 = taskManager.createSubtask("Купить еду", "Купить еду для праздника", epic1.getId());

        Epic epic2 = taskManager.createEpic("Покупка квартиры", "Покупка новой квартиры");
        Subtask subtask3 = taskManager.createSubtask("Собрать документы", "Собрать все необходимые документы", epic2.getId());

        // Вывод списка задач, подзадач и эпиков
        System.out.println("Задачи:");
        System.out.println(taskManager.getAllTasks());

        System.out.println("Подзадачи:");
        System.out.println(taskManager.getAllSubtasks());

        System.out.println("Эпики:");
        System.out.println(taskManager.getAllEpics());

        // Изменение статусов
        task1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        epic1.updateStatus(); // Обновляем статус эпика на основе подзадач

        // Вывод статусов
        System.out.println("Статусы после изменений:");
        System.out.println("Task1: " + task1.getStatus());
        System.out.println("Subtask1: " + subtask1.getStatus());
        System.out.println("Subtask2: " + subtask2.getStatus());
        System.out.println("Epic1: " + epic1.getStatus());

        // Удаление задачи и эпика
        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic1.getId());

        // Вывод списков после удаления
        System.out.println("Список задач после удаления:");
        System.out.println(taskManager.getAllTasks());

        System.out.println("Список эпиков после удаления:");
        System.out.println(taskManager.getAllEpics());
    }
}
