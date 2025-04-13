package com.example.studentassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentassistant.R;
import com.example.studentassistant.data.DatabaseClient;
import com.example.studentassistant.data.entity.Student;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tvGreeting;
    private EditText etName;
    private Button btnSaveName;
    private Button btnSchedule;
    private Button btnReminders;
    private Button btnExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Сховати ActionBar
        setContentView(R.layout.activity_dashboard);

        tvGreeting = findViewById(R.id.tvGreeting);
        etName = findViewById(R.id.etName);
        btnSaveName = findViewById(R.id.btnSaveName);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnReminders = findViewById(R.id.btnReminders);
        btnExpenses = findViewById(R.id.btnExpenses);

        // Завантаження імені студента
        new Thread(new Runnable() {
            @Override
            public void run() {
                Student student = DatabaseClient.getInstance(MainActivity.this)
                        .getAppDatabase()
                        .studentDao()
                        .getStudent();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (student != null && !student.name.isEmpty()) {
                            tvGreeting.setText("Welcome, " + student.name + "!");
                        }
                    }
                });
            }
        }).start();

        btnSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if (!name.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Student student = new Student(name);
                            DatabaseClient.getInstance(MainActivity.this)
                                    .getAppDatabase()
                                    .studentDao()
                                    .insert(student);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvGreeting.setText("Welcome, " + name + "!");
                                    Toast.makeText(MainActivity.this, "Welcome, " + name + "!", Toast.LENGTH_SHORT).show();
                                    etName.setText("");
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemindersActivity.class);
                startActivity(intent);
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpensesActivity.class);
                startActivity(intent);
            }
        });
    }
}