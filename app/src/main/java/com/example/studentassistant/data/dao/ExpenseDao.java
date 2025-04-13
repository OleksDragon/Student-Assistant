package com.example.studentassistant.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.studentassistant.data.entity.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);

    @Query("SELECT * FROM expenses")
    List<Expense> getAllExpenses();
}