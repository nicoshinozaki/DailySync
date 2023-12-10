package com.example.dailysync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;

public class JournalActivity extends AppCompatActivity {
    private Calendar calendar;
    private DateFormat dateFormat;
    private TextView currDate;
    private Date currentDate;
    private String currDateStr;
    private String currUser;

    private EditText journalEntry;
    private String journalText;

    private ListView journalList;
    private ArrayList<Journal> journalArray;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        //Set the Current Date Text View
        calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        currDateStr = dateFormat.format(currentDate);
        currDate = findViewById(R.id.currentDate);
        currDate.setText(currDateStr);

        //Designate User By UserID in Firebase Real-Time Database
        currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        journalList = findViewById(R.id.journalList);
        if (journalArray== null) {
            journalArray = new ArrayList<>();
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, journalArray);
        journalList.setAdapter(adapter);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("DailySync Users")
                .child(currUser);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                journalArray.clear(); // Clear the existing data

                // Iterate through the dataSnapshot to retrieve Journal objects
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    String journalEntry = dateSnapshot.getValue(String.class);
                    Journal journal = new Journal(date, journalEntry);
                    journalArray.add(journal);
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled as needed
            }
        });

        journalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                String currDate = journalArray.get(pos).currDate;
                String journalEntry = journalArray.get(pos).journalEntry;

                AlertDialog.Builder builder
                        =new AlertDialog
                        .Builder(JournalActivity.this);
                builder
                        .setTitle(currDate)
                        .setMessage(journalEntry)
                        .setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }
                        )
                        .setNegativeButton(
                                "DELETE",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(JournalActivity.this);
                                        confirmBuilder.setTitle("Confirmation")
                                                .setMessage("Are you sure you want to delete this entry?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        // User confirmed, proceed with deletion
                                                        journalArray.remove(pos);
                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                                                .child("DailySync Users")
                                                                .child(currUser)
                                                                .child(currDateStr);
                                                        reference.removeValue();
                                                        adapter.notifyDataSetChanged();
                                                        dialogInterface.dismiss();
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        // User canceled, do nothing
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                                .create()
                                                .show();
                                    }
                                }
                        )
                        .show();
            }
        });
    }

    public void saveJournal(View view) {
        journalEntry = findViewById(R.id.journalEntry);
        journalText = journalEntry.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("DailySync Users")
                .child(currUser)
                .child(currDateStr);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Entry already exists, handle accordingly
                    Toast.makeText(JournalActivity.this, "Journal entry already exists for today", Toast.LENGTH_SHORT).show();
                } else {
                    // Entry does not exist, save the new journal entry
                    reference.setValue(journalText);

                    // Document the journal and update the UI
                    documentJournal();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void documentJournal() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("DailySync Users")
                .child(currUser);

        // Save journal text to the database under the current date
        userRef.child(currDateStr).setValue(journalText);

        // Create a Journal object and add it to the array
        Journal newJournal = new Journal(currDateStr, journalText);
        journalArray.add(newJournal);

        Toast.makeText(JournalActivity.this, "Journal Entry Saved!", Toast.LENGTH_SHORT).show();
        journalEntry.setText("");

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }
}