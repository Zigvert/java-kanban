package model.tasks;

import model.util.Status;
import model.util.TypeTask;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private TypeTask typeTask;


    public Task(String name, String description, TypeTask typeTask) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.typeTask = typeTask;
    }


    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.typeTask = TypeTask.TASK;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }
}
