package ru.nsu.ksadov.find;

import java.io.Serializable;

/**
 * Заказ.
 */
public class Order implements Serializable {
    private int id;
    private volatile String status;

    public Order(int id) {
        this.id = id;
        this.status = "Created";
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
        System.out.println("[" + id + "] [" + status + "]");
    }
}
