package com.example.newnotes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context context;
    NoteAdapter noteAdapter;
    ArrayList<Note> noteArrayList = new ArrayList<>();

    public NoteAdapter(Context context, ArrayList<Note> noteArrayList){
        this.context = context;
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteArrayList.get(position);
        holder.noteCardTitle.setText(note.getTitle());
        holder.noteCardDescription.setText(note.getDescription());

        // MAKE BETTER POPUP MENU / CUSTOM ONE
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Log.d("NoteAdapter", "Show dialog...");
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_menu);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.box_shape);

                MaterialButton editButton = (MaterialButton) dialog.findViewById(R.id.editButton);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AddNoteActivity.class);
                        intent.putExtra("id", note.getId());
                        intent.putExtra("title", note.getTitle());
                        intent.putExtra("description", note.getDescription());
                        context.startActivity(intent);
                    }
                });

                MaterialButton deleteButton = (MaterialButton) dialog.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NoteHelper noteHelper = new NoteHelper(context);
                        noteHelper.deleteNote(note.getId());
                        noteArrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        dialog.dismiss();
                    }
                });

                MaterialButton exportButton = (MaterialButton) dialog.findViewById(R.id.exportButton);
                exportButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileName = note.getTitle() + ".txt";
                        String fileContents = note.getDescription();

                        File exportFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(exportFolder, fileName);

                        try{
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(fileContents.getBytes());
                            fileOutputStream.close();
                            Toast.makeText(context, "Note exported successfully", Toast.LENGTH_LONG).show();
                        }catch (IOException e){
                            Toast.makeText(context, "Note couldn't export", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });

                dialog.show();

//                PopupMenu menu = new PopupMenu(context, view);
//                menu.inflate(R.menu.popup_menu);
//                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if(item.getTitle().equals("EDIT")){
//                            // EDIT NOTE
//                            context.startActivity(new Intent(context, AddNoteActivity.class));
//
//                        }else if(item.getTitle().equals("DELETE")){
//                            // DELETE NOTE FORM DATABASE
//
//                            NoteHelper noteHelper = new NoteHelper(context);
//                            noteHelper.deleteNote(note.getTitle());
//                        }
//                        return true;
//                    }
//                });
//                menu.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView noteCardTitle;
        TextView noteCardDescription;
        TextView noteCardDateTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCardTitle = itemView.findViewById(R.id.noteCardTitle);
            noteCardDescription = itemView.findViewById(R.id.noteCardDescription);
            noteCardDateTime = itemView.findViewById(R.id.noteCardDatetime);
        }
    }
    public void setNoteAdapter(NoteAdapter noteAdapter){
        this.noteAdapter = noteAdapter;
    }



}
