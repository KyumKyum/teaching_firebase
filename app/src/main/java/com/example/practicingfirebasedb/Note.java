package com.example.practicingfirebasedb;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentID;
    private String title;
    private String description;

    public Note(){
        //Public no-args constructor required
    }

    public Note(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }
}
