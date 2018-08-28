package com.example.razvan.licenta.Menu;

public class OverviewListView {
    private String name;
    private double amount;
    private String date;
    private String note;


    public OverviewListView(String name, double amount, String date, String note){
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.note = note;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getNote() { return note;}

    public void setNote(String note) { this.note = note; }


}

