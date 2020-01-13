package com.dauntlessdev.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class NotesActivity extends AppCompatActivity {
    TextView input;
    String currInput;
    Intent intent;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        sharedPreferences = this.getSharedPreferences("com.dauntlessdev.notes", Context.MODE_PRIVATE);

        input = findViewById(R.id.editText);
        intent = getIntent();
        if (MainActivity.clicked){
            currInput = intent.getStringExtra("Note");
            input.setText(currInput);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (currInput == null) {
            MainActivity.noteList.add(input.getText().toString());
            MainActivity.arrayAdapter.notifyDataSetChanged();
            MainActivity.clicked = false;
        }else{
            MainActivity.noteList.set(intent.getIntExtra("Position", 0),input.getText().toString());
            MainActivity.arrayAdapter.notifyDataSetChanged();
            MainActivity.clicked = false;

        }

        try {
            sharedPreferences.edit().putString("NoteList", ObjectSerializer.serialize(MainActivity.noteList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
