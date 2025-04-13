package com.example.studentassistant.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.studentassistant.data.dao.ExpenseDao;
import com.example.studentassistant.data.dao.LessonDao;
import com.example.studentassistant.data.dao.ReminderDao;
import com.example.studentassistant.data.dao.StudentDao;
import com.example.studentassistant.data.entity.Expense;
import com.example.studentassistant.data.entity.Lesson;
import com.example.studentassistant.data.entity.Reminder;
import com.example.studentassistant.data.entity.Student;

@Database(entities = {Student.class, Lesson.class, Reminder.class, Expense.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
    public abstract LessonDao lessonDao();
    public abstract ReminderDao reminderDao();
    public abstract ExpenseDao expenseDao();
}
