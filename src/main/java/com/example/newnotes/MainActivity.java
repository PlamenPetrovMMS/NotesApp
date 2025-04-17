package com.example.newnotes;

import android.annotation.SuppressLint;
import android.app.ComponentCaller;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    TextView mainTitle;
    RelativeLayout layout;
    FloatingActionButton addButton, settingsButton;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> noteArrayList = new ArrayList<>();
    NoteHelper noteHelper;

    @Override
    public void onNewIntent(@NonNull Intent intent, @NonNull ComponentCaller caller) {
        super.onNewIntent(intent, caller);
//        setIntent(intent);
//        handleImportedTextFile(intent);

        Log.d("ON NEW INTENT", "INTENT RECEIVED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("ON CREATE", "INTENT RECEIVED");

        // The theme should be set before the activity is created !!
        sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        int selectedTheme = sharedPreferences.getInt("selected_theme", R.style.Theme_NewNotes_Theme1); // default to Theme 1
        setTheme(selectedTheme);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteHelper = new NoteHelper(this);

        Cursor cursor = noteHelper.showData();
        while(cursor.moveToNext()){
            Note note = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            noteArrayList.add(note);
        }

        noteAdapter = new NoteAdapter(this, noteArrayList);
        recyclerView.setAdapter(noteAdapter);

        // IMPORT .T

        // CHECK IF ACTIVITY WAS LAUNCHED VIA SHARE INTENT / SHOULD BE REWORKED



        layout = (RelativeLayout) findViewById(R.id.main);

        mainTitle = (TextView) findViewById(R.id.mainTitle);

        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
                finish();
            }
        });

        settingsButton = (FloatingActionButton) findViewById(R.id.noteSettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.settings_menu);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.box_shape);

                FloatingActionButton themeColorButton1, themeColorButton2, themeColorButton3;
                MaterialButton italicFontButton, boldFontButton, normalFontButton;

                themeColorButton1 = (FloatingActionButton) dialog.findViewById(R.id.themeColorButton1);
                themeColorButton2 = (FloatingActionButton) dialog.findViewById(R.id.themeColorButton2);
                themeColorButton3 = (FloatingActionButton) dialog.findViewById(R.id.themeColorButton3);

                themeColorButton1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
                        sharedPreferences.edit().putInt("selected_theme", R.style.Theme_NewNotes_Theme1).apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialog.dismiss();
                        recreate();
//                        startActivity(intent);
//                        finish();
                    }
                });

                themeColorButton2.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
                        sharedPreferences.edit().putInt("selected_theme", R.style.Theme_NewNotes_Theme2).apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialog.dismiss();
//                        startActivity(intent);
//                        finish();
                        recreate();
                    }
                });

                themeColorButton3.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
                        sharedPreferences.edit().putInt("selected_theme", R.style.Theme_NewNotes_Theme3).apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialog.dismiss();
//                        startActivity(intent);
//                        finish();
                        recreate();
                    }
                });

                italicFontButton = (MaterialButton) dialog.findViewById(R.id.italicFontButton);
                boldFontButton = (MaterialButton) dialog.findViewById(R.id.boldFontButton);
                normalFontButton = (MaterialButton) dialog.findViewById(R.id.normalFontButton);

                italicFontButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences("fonts_prefs", MODE_PRIVATE);
                        sharedPreferences.edit().putString("selected_font", "italic").apply();
                        setFont("italic");
                    }
                });

                boldFontButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences("fonts_prefs", MODE_PRIVATE);
                        sharedPreferences.edit().putString("selected_font", "bold").apply();
                        setFont("bold");
                    }
                });

                normalFontButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences("fonts_prefs", MODE_PRIVATE);
                        sharedPreferences.edit().putString("selected_font", "normal").apply();
                        setFont("normal");
                    }
                });
                dialog.show();
            }
        });

        String selectedFont = sharedPreferences.getString("selected_font", "normal");
        System.out.println("Selected font: " + selectedFont);
        setFont(selectedFont);

        handleImportedTextFile(getIntent());
    }
    public void setFont(String font){
        int typefaceStyle = Typeface.NORMAL;

        switch(font){
            case "italic":
                typefaceStyle = Typeface.ITALIC;
                break;
            case "bold":
                typefaceStyle = Typeface.BOLD;
                break;
            default:
                typefaceStyle = Typeface.NORMAL;
                break;
        }

        mainTitle.setTypeface(mainTitle.getTypeface(), typefaceStyle);
    }

    private void readSharedFile(Uri fileUri){
        try{
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
            reader.close();
            // infinite loop ??

            Note importedNote = convertFileToNote(fileUri);
            noteArrayList.add(importedNote);
            noteAdapter.notifyDataSetChanged();

            Toast.makeText(this, "File imported successfully", Toast.LENGTH_LONG).show();
        }catch (IOException e){
            Toast.makeText(this, "Error on impoting files", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private Note convertFileToNote(Uri fileUri){
        String fileTitle = getFileName(fileUri).replace(".txt", "");
        String fileContent = getFileContent(fileUri);
        long newNoteId = noteHelper.insertNote(fileTitle, fileContent);
        return new Note((int) newNoteId, fileTitle, fileContent);
    }
    private String getFileContent(Uri fileUri){
        StringBuilder stringBuilder = new StringBuilder();

        try(InputStream inputStream = getContentResolver().openInputStream(fileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            String line = null;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return stringBuilder.toString().trim();
    }
    private String getFileName(Uri fileUri){
        String fileName = null;

        if(fileUri.getScheme().equals("content")){
            try(Cursor cursor = getContentResolver().query(fileUri, null, null, null, null )){
                if(cursor != null && cursor.moveToFirst()){
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                }
            }
        }
        return fileName;
    }

    private void handleImportedTextFile(Intent intent){
        if(Intent.ACTION_SEND.equals(intent.getAction())){
            if(intent.getType() != null && intent.getType().equals("text/plain")){
                Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if(fileUri != null){
                    readSharedFile(fileUri);
                }
            }
        }else if(Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction())){
            if(intent.getType() != null && intent.getType().equals("text/plain")){
                ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                if(fileUris != null){
                    for(Uri fileUri : fileUris){
                        readSharedFile(fileUri);
                    }
                }
            }
        }
    }
}