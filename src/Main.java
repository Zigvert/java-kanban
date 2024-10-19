package tasks;

import controllers.Managers;
import controllers.TaskManager;
import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;
import model.util.Status;
import model.util.TypeTask;

public class Main {
        public static void main(String[] args) {
                TaskManager tm = Managers.getDefault();


                Task task1 = new Task("Задача 1", "Описание 1", TypeTask.TASK);
                Task task2 = new Task("Задача 2", "Описание 2", TypeTask.TASK);
                tm.setTask(task1);
                tm.setTask(task2);


                Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
                tm.setTask(epic1);


                Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId(), 3);
                Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1.getId(), 4);
                Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.NEW, epic1.getId(), 5);

                tm.setTask(subtask1);
                tm.setTask(subtask2);
                tm.setTask(subtask3);


                epic1.addSubtask(subtask1);
                epic1.addSubtask(subtask2);

                Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
                tm.setTask(epic2); // id 6
                epic2.addSubtask(subtask3);


                System.out.println("Все задачи: " + tm.getAllTasks());
                System.out.println("Все подзадачи: " + tm.getAllSubtasks());
                System.out.println("Все эпики: " + tm.getAllEpics());


                tm.updateTask(new Task("Новое имя Задачи 1", "Новое описание задачи 1", Status.DONE, task1.getId()));
                tm.updateTask(new Subtask("Новое имя подзадачи 1", "Новое описание", Status.IN_PROGRESS, subtask1.getEpicId(), subtask1.getId()));
                tm.updateTask(new Subtask(subtask3.getName(), subtask3.getDescription(), Status.DONE, subtask3.getEpicId(), subtask3.getId()));


                System.out.println("..............");
                System.out.println("Эпики: " + tm.getAllEpics());
                System.out.println("Подзадачи эпика 1: " + tm.getSubtaskEpic(epic1));


                System.out.println(".............");
                tm.removeTaskById(task1.getId()); // Удаление задачи по id
                tm.removeTaskById(epic2.getId()); // Удаление эпика по id
                System.out.println("Все задачи после удаления: " + tm.getAllTasks());
                System.out.println("Все подзадачи после удаления: " + tm.getAllSubtasks());
                System.out.println("Все эпики после удаления: " + tm.getAllEpics());

                // Проверка истории
                System.out.println("////////");
                for (int i = 0; i < 15; i++) {
                        tm.getTaskById(subtask1.getId());
                }
                tm.getTaskById(subtask2.getId());
                int i = 1;
                System.out.println("История задач:");
                for (Task arg : tm.getHistory()) {
                        System.out.println(arg.getId() + " " + i++);
                }
        }
}
