package com.example.to_dolistapp.DatabaseHandler;

public class TaskClass {

    private int id;
    private String title, description, date, time, category, status;

    public TaskClass(int id, String title, String description, String date, String time, String category, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.category = category;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTaskTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }
}
