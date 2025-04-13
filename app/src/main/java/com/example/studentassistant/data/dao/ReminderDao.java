package com.example.studentassistant.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.studentassistant.data.entity.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {
    @Insert
    void insert(Reminder reminder);

    @Query("SELECT * FROM reminders")
    List<Reminder> getAllReminders();
}