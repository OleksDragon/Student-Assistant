package com.example.studentassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentassistant.R;
import com.example.studentassistant.data.DatabaseClient;
import com.example.studentassistant.data.entity.Lesson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private static final String TAG = "ScheduleActivity";
    private CalendarView calendarView;
    private ListView listView;
    private SeekBar seekBar;
    private ArrayAdapter<String> adapter;
    private final List<String> lessons = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Сховати ActionBar
        setContentView(R.layout.activity_schedule);

        calendarView = findViewById(R.id.calendarView);
        listView = findViewById(R.id.lvSchedule);
        seekBar = findViewById(R.id.sbLessonsCount);
        Button btnHome = findViewById(R.id.btnHome);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        // Завантажити всі уроки на початку
        loadAllLessons();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                loadLessons(selectedDate);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int lessonsToShow = progress + 1;
                updateListView(lessonsToShow);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadLessons(String date) {
        new Thread(() -> {
            List<Lesson> lessonList = DatabaseClient.getInstance(ScheduleActivity.this)
                    .getAppDatabase()
                    .lessonDao()
                    .getLessonsByDate(date);
            List<String> newLessons = new ArrayList<>();
            for (Lesson lesson : lessonList) {
                newLessons.add(lesson.name + " (" + lesson.date + ")");
            }
            runOnUiThread(() -> {
                synchronized (lessons) {
                    lessons.clear();
                    lessons.addAll(newLessons);
                }
                int lessonsToShow = seekBar.getProgress() + 1;
                updateListView(lessonsToShow);
            });
        }).start();
    }

    private void loadAllLessons() {
        new Thread(() -> {
            List<Lesson> lessonList = DatabaseClient.getInstance(ScheduleActivity.this)
                    .getAppDatabase()
                    .lessonDao()
                    .getAllLessons();
            List<String> newLessons = new ArrayList<>();
            for (Lesson lesson : lessonList) {
                newLessons.add(lesson.name + " (" + lesson.date + ")");
            }
            runOnUiThread(() -> {
                synchronized (lessons) {
                    lessons.clear();
                    lessons.addAll(newLessons);
                }
                int lessonsToShow = seekBar.getProgress() + 1;
                updateListView(lessonsToShow);
            });
        }).start();
    }

    private void updateListView(int lessonsToShow) {
        List<String> displayLessons;
        synchronized (lessons) {
            displayLessons = new ArrayList<>(lessons);
        }
        adapter.clear();
        if (!displayLessons.isEmpty()) {
            adapter.addAll(displayLessons.subList(0, Math.min(lessonsToShow, displayLessons.size())));
        }
        adapter.notifyDataSetChanged();
    }
}