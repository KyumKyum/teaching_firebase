package com.example.practicingfirebasedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private final static String KEY_TITLE = "TITLE";
    private final static String KEY_DESCRIPTION = "DESCRIPTION";

    private EditText inputTitle;
    private EditText inputDescription;
    private TextView textView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("TODOLIST");
    //private DocumentReference docRef = db.collection("TODOLIST").document("DOC");

//    private ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTitle = findViewById(R.id.ET_Title);
        inputDescription = findViewById(R.id.ET_Description);
        textView = findViewById(R.id.TV_TextHere);
    }

    @Override
    protected void onStart() { //Attach listener
        super.onStart();

       /*listener =  colRef.document("First To Do List").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Toast.makeText(MainActivity.this, "Load Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(documentSnapshot.exists()){
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();

                    textView.setText("Title: "+title+"\nDescription: "+description);
                }
            }
        }); Manually*/

        colRef.document("First To Do List").addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Toast.makeText(MainActivity.this, "Load Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(documentSnapshot.exists()){
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();

                    textView.setText("Title: "+title+"\nDescription: "+description);
                }
            }
        });

        colRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Toast.makeText(MainActivity.this, "Load Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String data = "";

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentID(documentSnapshot.getId());

                    String title = note.getTitle();
                    String description = note.getDescription();

                    data += "Title: "+ title + "\nDescription: " + description + "\n----------\n";
                }
                textView.setText(data);

            }
        });
    }

//    @Override
//    protected void onStop() { //Manually
//        super.onStop();
//        listener.remove(); //detach listener
//    }

    public void saveNote(View v){
        String title = inputTitle.getText().toString();
        String description = inputDescription.getText().toString();

        Note note = new Note(title,description);

        colRef.document("First To Do List").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Save Complete!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadNote(View v){
        colRef.document("First To Do List").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Note note = documentSnapshot.toObject(Note.class);
                            String title = note.getTitle();
                            String description = note.getDescription();

                            textView.setText("Title: "+title+"\nDescription: "+description);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addNote(View v){ //add multiple notes
        String title = inputTitle.getText().toString();
        String description = inputDescription.getText().toString();

        Note note = new Note(title,description);

        colRef.add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadNotes(View v){
        colRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentID(documentSnapshot.getId());

                            String title = note.getTitle();
                            String description = note.getDescription();

                            data += "Title: "+ title + "\nDescription: " + description + "\n----------\n";
                        }
                        textView.setText(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Simple Queries

        /*colRef.whereEqualTo("title","Do my job")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentID(documentSnapshot.getId());

                            String title = note.getTitle();
                            String description = note.getDescription();

                            data += "Title: "+ title + "\nDescription: " + description + "\n----------\n";
                        }
                        textView.setText(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
    }
}