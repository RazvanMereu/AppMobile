package com.example.razvan.licenta.Expenses;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.razvan.licenta.Database.DatabaseHelper;
import com.example.razvan.licenta.R;
import com.example.razvan.licenta.Settings.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseAdd extends Activity implements AdapterView.OnItemSelectedListener {

    EditText amount_add;
    EditText date_add;
    EditText note;
    Button button;
    Button buttonCancel;
    Calendar calendar;
    Spinner spinner;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseUser mUser;
    private int year, month, day;
    final Context context = this;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab4);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Categories");
        amount_add = (EditText) findViewById(R.id.editTextAmount);
        date_add = (EditText) findViewById(R.id.date);
        note = (EditText) findViewById(R.id.editNote);
        button = (Button) findViewById(R.id.btnAddExpense);
        buttonCancel = (Button) findViewById(R.id.btnCancelExpense);
        spinner = (Spinner) findViewById(R.id.list_spinner);
        spinner.setOnItemSelectedListener(this);

        final List<String> labels = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category category = new Category(ds.getKey(), ds.getValue() != null
                            ? ds.getValue().toString() : null);
                    labels.add(category.getName());
                }
                loadListView(labels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EROARE: ", databaseError.getMessage());
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialog);

        alertDialogBuilder
                .setCancelable(false)
                .

                        setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        amount_add.setText(userInput.getText());
                                    }
                                }).
                setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        amount_add.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialog);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        amount_add.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View arg0) {
                Log.e("spinner is: ", spinner.toString());
                String category_add = spinner.getSelectedItem().toString();
                String amount = amount_add.getText().toString();
                String date = date_add.getText().toString();
                String notes = note.getText().toString();

                if (category_add.trim().length() > 0) {

                    Expense expense = new Expense(category_add, amount,notes, date);
                    DatabaseHelper db = new DatabaseHelper();
                    db.saveExpense(mUser, expense);

                    amount_add.setText("");
                    date_add.setText("");
                    note.setText("");

//                    InputMethodManager imm = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
//
//                    InputMethodManager imm_amount = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm_amount.hideSoftInputFromWindow(amount_add.getWindowToken(), 0);
//
//                    InputMethodManager imm_date = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm_date.hideSoftInputFromWindow(date_add.getWindowToken(), 0);
//
//                    InputMethodManager imm_note = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm_note.hideSoftInputFromWindow(note.getWindowToken(), 0);
                    Toast.makeText(ExpenseAdd.this, "New Expense Added!", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Please enter label name",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadListView(List<String> labels) {
//        DBHelper dbHelper = new DBHelper(getApplicationContext());

        Log.e("lungime label", String.valueOf(labels.size()));
        for (String label : labels) {
            Log.e("mesaj", label);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(dataAdapter);

    }

    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "Choose date", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        date_add.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();
        Log.e("You selected : ", label);
        spinner.getSelectedItem().toString();
        Toast.makeText(parent.getContext(), "You Selected: " + label, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Please select category", Toast.LENGTH_LONG).show();
    }
}


