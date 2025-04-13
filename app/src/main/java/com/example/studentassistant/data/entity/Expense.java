package com.example.studentassistant.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public double amount;
    public String categories;
    public String paymentMethod;

    public Expense(double amount, String categories, String paymentMethod) {
        this.amount = amount;
        this.categories = categories;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", amount=" + amount + ", categories='" + categories + "', paymentMethod='" + paymentMethod + "'}";
    }
}