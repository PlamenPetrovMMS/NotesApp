package com.example.newnotes;

public class Note {
    int id;
    String title;
    String description;
    long createdTime;

    // ADD NOTE's CREATED DATE AND TIME

    public Note(int id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setCreatedTime(long createdTime){
        this.createdTime = createdTime;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public long getCreatedTime(){
        return createdTime;
    }
    public int getId(){
        return id;
    }
}
