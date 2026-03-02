package ru.nsu.ksadov.find;

import java.io.Serializable;

public class Order implements Serializable{
    private final int id;
    private String status;

    public Order(int id) {
        this.id = id;
        this.status = "Created";
    }

    public int getId() {
        return id;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
        System.out.println("[" + id + "] [" + status + "]");
    }
}
