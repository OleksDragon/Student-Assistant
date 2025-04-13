package com.example.studentassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentassistant.R;
import com.example.studentassistant.data.DatabaseClient;
import com.example.studentassistant.data.entity.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpensesActivity extends AppCompatActivity {
    private EditText etAmount;
    private CheckBox cbFood, cbTransport, cbEntertainment, cbEducation;
    private RadioGroup rgPaymentMethod;
    private Button btnSaveExpense;
    private ListView lvExpenses;
    private ArrayAdapter<String> adapter;
    private List<String> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Сховати ActionBar
        setContentView(R.layout.activity_expenses);

        etAmount = findViewById(R.id.etAmount);
        cbFood = findViewById(R.id.cbFood);
        cbTransport = findViewById(R.id.cbTransport);
        cbEntertainment = findViewById(R.id.cbEntertainment);
        cbEducation = findViewById(R.id.cbEducation);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        lvExpenses = findViewById(R.id.lvExpenses);
        Button btnHome = findViewById(R.id.btnHome);

        expenseList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        lvExpenses.setAdapter(adapter);

        // Завантаження всіх витрат
        loadExpenses();

        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = etAmount.getText().toString();
                if (amountStr.isEmpty()) {
                    Toast.makeText(ExpensesActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(ExpensesActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder categoriesBuilder = new StringBuilder();
                if (cbFood.isChecked()) categoriesBuilder.append("Food ");
                if (cbTransport.isChecked()) categoriesBuilder.append("Transport ");
                if (cbEntertainment.isChecked()) categoriesBuilder.append("Entertainment ");
                if (cbEducation.isChecked()) categoriesBuilder.append("Education ");
                final String categories = categoriesBuilder.toString().isEmpty() ? "No category" : categoriesBuilder.toString();

                int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
                final String paymentMethod = selectedId == R.id.rbCash ? "Cash" : "Card";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Expense expense = new Expense(amount, categories, paymentMethod);
                        DatabaseClient.getInstance(ExpensesActivity.this)
                                .getAppDatabase()
                                .expenseDao()
                                .insert(expense);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ExpensesActivity.this,
                                        "Expense: " + amount + " UAH, " + categories + ", " + paymentMethod,
                                        Toast.LENGTH_SHORT).show();
                                etAmount.setText("");
                                cbFood.setChecked(false);
                                cbTransport.setChecked(false);
                                cbEntertainment.setChecked(false);
                                cbEducation.setChecked(false);
                                rgPaymentMethod.clearCheck();
                                loadExpenses();
                            }
                        });
                    }
                }).start();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpensesActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadExpenses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Expense> expenses = DatabaseClient.getInstance(ExpensesActivity.this)
                        .getAppDatabase()
                        .expenseDao()
                        .getAllExpenses();
                expenseList.clear();
                for (Expense expense : expenses) {
                    expenseList.add(expense.amount + " UAH, " + expense.categories + ", " + expense.paymentMethod);
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