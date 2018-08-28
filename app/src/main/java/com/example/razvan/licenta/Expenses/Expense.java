package com.example.razvan.licenta.Expenses;

public class Expense {

    private String category;
    private String amount;
    private String note;
    private String date;

    public Expense(String category, String amount, String note, String date) {
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


