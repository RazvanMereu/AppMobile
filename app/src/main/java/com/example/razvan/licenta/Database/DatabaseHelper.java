package com.example.razvan.licenta.Database;

import com.example.razvan.licenta.Expenses.Expense;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    private FirebaseUser myUser;

    public DatabaseHelper() {
        myUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void saveExpense(FirebaseUser mUser, Expense expense) {
        DatabaseReference expenseReference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Expenses").push();

        expenseReference.child("Category").setValue(expense.getCategory());
        expenseReference.child("Amount").setValue(expense.getAmount());
        expenseReference.child("Date").setValue(expense.getDate());
        expenseReference.child("Note").setValue(expense.getNote());
    }
}
