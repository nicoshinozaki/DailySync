package com.example.dailysync;

public class Journal {
    public String currDate;
    public String journalEntry;

    public Journal(String currDate, String journalEntry) {
        this.currDate = currDate;
        this.journalEntry = journalEntry;
    }

    @Override
    public String toString() {return currDate;}
}
