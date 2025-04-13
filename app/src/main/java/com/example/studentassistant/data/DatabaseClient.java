package com.example.studentassistant.data;

import android.content.Context;
import androidx.room.Room;
import com.example.studentassistant.data.entity.Expense;
import com.example.studentassistant.data.entity.Lesson;
import com.example.studentassistant.data.entity.Reminder;
import com.example.studentassistant.data.entity.Student;

public class DatabaseClient {
    private static DatabaseClient instance;
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "student_db")
                .build();

        // Ініціалізація початкових даних
        initializeData();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    private void initializeData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Перевіряємо, чи є дані в таблиці students
                if (appDatabase.studentDao().getStudent() == null) {
                    insertInitialData();
                }
            }
        }).start();
    }

    private void insertInitialData() {
        // Додаємо тестового студента
        Student student = new Student("Oleksandr");
        appDatabase.studentDao().insert(student);

        // Додаємо тестові уроки
        Lesson lesson1 = new Lesson("Android development", "13-4-2025");
        Lesson lesson2 = new Lesson("Java development", "13-4-2025");
        Lesson lesson3 = new Lesson("C# development", "14-4-2025");
        appDatabase.lessonDao().insert(lesson1);
        appDatabase.lessonDao().insert(lesson2);
        appDatabase.lessonDao().insert(lesson3);

        // Понеділок, 14 квітня
        appDatabase.lessonDao().insert(new Lesson("Web Development", "14-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Windows Programming", "14-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Database Systems", "14-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Software Testing", "14-4-2025"));

        // Вівторок, 15 квітня
        appDatabase.lessonDao().insert(new Lesson("Algorithms", "15-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Cloud Computing", "15-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("UI/UX Design", "15-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Mobile App Development", "15-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Cybersecurity", "15-4-2025"));

        // Середа, 16 квітня
        appDatabase.lessonDao().insert(new Lesson("Machine Learning", "16-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Data Structures", "16-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Backend Development", "16-4-2025"));

        // Четвер, 17 квітня
        appDatabase.lessonDao().insert(new Lesson("DevOps", "17-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Software Architecture", "17-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Version Control", "17-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Agile Methodologies", "17-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("API Development", "17-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Networking", "17-4-2025"));

        // П'ятниця, 18 квітня
        appDatabase.lessonDao().insert(new Lesson("Software Debugging", "18-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Project Management", "18-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Frontend Development", "18-4-2025"));
        appDatabase.lessonDao().insert(new Lesson("Quality Assurance", "18-4-2025"));

        // Додаємо тестові нагадування
        Reminder reminder1 = new Reminder("Prepare a presentation", "10:00");
        Reminder reminder2 = new Reminder("Meeting with the teacher", "14:30");
        appDatabase.reminderDao().insert(reminder1);
        appDatabase.reminderDao().insert(reminder2);

        // Додаємо тестові витрати
        Expense expense1 = new Expense(150.0, "Food", "Cash");
        Expense expense2 = new Expense(200.0, "Transport", "Card");
        Expense expense3 = new Expense(900.0, "Education", "Card");
        appDatabase.expenseDao().insert(expense1);
        appDatabase.expenseDao().insert(expense2);
        appDatabase.expenseDao().insert(expense3);
    }
}