package com.example.studentassistant.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String taskName;
    public String time;

    public Reminder(String taskName, String time) {
        this.taskName = taskName;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reminder{id=" + id + ", taskName='" + taskName + "', time='" + time + "'}";
    }
}