package com.dauntlessdev.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    static ArrayList<String> noteList;
    static ArrayAdapter<String> arrayAdapter;
    static boolean clicked;
    ListView listView;
    Intent intent;
    int itemPosition;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.dauntlessdev.notes", Context.MODE_PRIVATE);
        try {
            noteList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("NoteList", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent = new Intent(this, NotesActivity.class);

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemPosition = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete?")
                        .setMessage("Are you sure to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        noteList.remove(itemPosition);
                                        arrayAdapter.notifyDataSetChanged();

                                        try {
                                            sharedPreferences.edit().putString("NoteList", ObjectSerializer.serialize(noteList)).apply();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        ).setNegativeButton("No",null)
                        .show();

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("Note" , noteList.get(position));
                intent.putExtra("Position", position);
                clicked = true;
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //add notes
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

}
