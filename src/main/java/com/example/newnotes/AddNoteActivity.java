package com.example.newnotes;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddNoteActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    int id;
    EditText titleInput;
    EditText descriptionInput;
    FloatingActionButton saveButton, cancelButton;
    NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // The theme should be set before the activity is created !!
        sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("selected_theme", R.style.Theme_NewNotes_Theme1);
        setTheme(theme);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleInput = (EditText) findViewById(R.id.noteTitle);
        descriptionInput = (EditText) findViewById(R.id.noteDescription);

        id = getIntent().getIntExtra("id", -1);
        titleInput.setText(getIntent().getStringExtra("title"));
        descriptionInput.setText(getIntent().getStringExtra("description"));

        noteHelper = new NoteHelper(this);

        saveButton = (FloatingActionButton) findViewById(R.id.noteSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleInput.length() > 0 && descriptionInput.length() > 0) {
                    if(id != -1){
                        noteHelper.updateNote(id, titleInput.getText().toString(), descriptionInput.getText().toString());
                        Toast.makeText(AddNoteActivity.this, "Note edited", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        noteHelper.insertNote(titleInput.getText().toString(), descriptionInput.getText().toString());
                        Toast.makeText(AddNoteActivity.this, "New note created", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
                finish();
            }
        });

        cancelButton = (FloatingActionButton) findViewById(R.id.noteCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}