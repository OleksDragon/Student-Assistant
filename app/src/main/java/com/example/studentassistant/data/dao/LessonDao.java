package com.example.studentassistant.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.studentassistant.data.entity.Lesson;

import java.util.List;

@Dao
public interface LessonDao {
    @Insert
    void insert(Lesson lesson);

    @Query("SELECT * FROM lessons WHERE date = :date")
    List<Lesson> getLessonsByDate(String date);

    @Query("SELECT * FROM lessons")
    List<Lesson> getAllLessons();
}