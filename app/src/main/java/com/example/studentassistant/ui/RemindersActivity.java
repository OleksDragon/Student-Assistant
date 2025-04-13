package com.example.studentassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentassistant.R;
import com.example.studentassistant.data.DatabaseClient;
import com.example.studentassistant.data.entity.Reminder;

import java.util.ArrayList;
import java.util.List;

public class RemindersActivity extends AppCompatActivity {
    private EditText etTaskName;
    private TimePicker timePicker;
    private Button btnAddReminder;
    private ListView lvReminders;
    private ArrayAdapter<String> adapter;
    private List<String> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Сховати ActionBar
        setContentView(R.layout.activity_reminders);

        etTaskName = findViewById(R.id.etTaskName);
        timePicker = findViewById(R.id.timePicker);
        btnAddReminder = findViewById(R.id.btnAddReminder);
        lvReminders = findViewById(R.id.lvReminders);
        Button btnHome = findViewById(R.id.btnHome);

        reminderList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderList);
        lvReminders.setAdapter(adapter);

        // Загрузка всех напоминаний
        loadReminders();

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = etTaskName.getText().toString();
                if (taskName.isEmpty()) {
                    Toast.makeText(RemindersActivity.this, "Please enter a task name", Toast.LENGTH_SHORT).show();
                    return;
                }
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String time = hour + ":" + String.format("%02d", minute);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Reminder reminder = new Reminder(taskName, time);
                        DatabaseClient.getInstance(RemindersActivity.this)
                                .getAppDatabase()
                                .reminderDao()
                                .insert(reminder);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RemindersActivity.this,
                                        "Reminder '" + taskName + "' at " + time + " added",
                                        Toast.LENGTH_SHORT).show();
                                etTaskName.setText("");
                                loadReminders();
                            }
                        });
                    }
                }).start();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemindersActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadReminders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Reminder> reminders = DatabaseClient.getInstance(RemindersActivity.this)
                        .getAppDatabase()
                        .reminderDao()
                        .getAllReminders();
                reminderList.clear();
                for (Reminder reminder : reminders) {
                    reminderList.add(reminder.taskName + " at " + reminder.time);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}