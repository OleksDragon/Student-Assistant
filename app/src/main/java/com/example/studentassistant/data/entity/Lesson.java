package com.example.studentassistant.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lessons")
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String date;

    public Lesson(String name, String date) {
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Lesson{id=" + id + ", name='" + name + "', date='" + date + "'}";
    }
}