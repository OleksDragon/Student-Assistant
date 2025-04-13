package com.example.studentassistant.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.studentassistant.data.entity.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void insert(Student student);

    @Query("SELECT * FROM students LIMIT 1")
    Student getStudent();

    @Query("SELECT * FROM students")
    List<Student> getAllStudents();
}