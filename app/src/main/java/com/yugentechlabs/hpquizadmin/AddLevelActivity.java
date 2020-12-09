package com.yugentechlabs.hpquizadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yugentechlabs.hpquizadmin.Model.Level;

import java.util.ArrayList;

public class AddLevelActivity extends AppCompatActivity {

    EditText question, option1,option2, option3,option4,level;
    Button next, submit;
    TextView quesNum;
    ArrayList<String> levelList;
    int c,levelNumber;
    FirebaseFirestore db;
    ProgressDialog progress;
    public static final String TAG="fire";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_level);

        question=findViewById(R.id.question);
        option1=findViewById(R.id.option_one);
        option2=findViewById(R.id.option_two);
        option3=findViewById(R.id.option_three);
        option4=findViewById(R.id.option_four);
        next=findViewById(R.id.next);
        submit=findViewById(R.id.submit);
        quesNum=findViewById(R.id.question_num);
        level=findViewById(R.id.level);


        levelList=new ArrayList<String>();
        c=1;

        db = FirebaseFirestore.getInstance();

       //getLevelfromDB();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(question.getText().toString().equals("") || option1.getText().toString().equals("") || option2.getText().toString().equals("") || option3.getText().toString().equals("") || option4.getText().toString().equals(""))) {
                    if (c < 10) {
                        c++;
                        nextPressed();
                    } else {
                        next.setEnabled(false);
                        submit.setVisibility(View.VISIBLE);
                    }
                }
            else{
                Toast.makeText(AddLevelActivity.this, "Fill the details", Toast.LENGTH_SHORT).show();
            }
        }});


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(question.getText().toString().equals("") || option1.getText().toString().equals("") || option2.getText().toString().equals("") || option3.getText().toString().equals("") || option4.getText().toString().equals(""))) {
                    submitPressed();
                }
                else{
                    Toast.makeText(AddLevelActivity.this, "Fill the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void submitPressed() {

        levelList.add(question.getText().toString());
        levelList.add(option1.getText().toString());
        levelList.add(option2.getText().toString());
        levelList.add(option3.getText().toString());
        levelList.add(option4.getText().toString());

        progress=new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.show();

        checkLevelFromDB();
    }



    private void addLeveltoDB() {

        Level level=new Level(levelList,levelNumber);

        db.collection("WizardingQuizLevels").document(String.valueOf(levelNumber)).set(level).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress.dismiss();
                Toast.makeText(AddLevelActivity.this, "Level Added successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddLevelActivity.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(AddLevelActivity.this, "Failed to add level.", Toast.LENGTH_SHORT).show();

            }
        });
        }


    private void checkLevelFromDB() {

        DocumentReference docRef = db.collection("WizardingQuizLevels").document(level.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                progress.dismiss();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(AddLevelActivity.this, "Level Already Exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        //Log.d(TAG, "No such document");
                        addLeveltoDB();
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(AddLevelActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(AddLevelActivity.this,MainActivity.class));
            }
        });

    }





    private void nextPressed() {
       quesNum.setText(("Question "+String.valueOf(c)));
        levelList.add(question.getText().toString());
        levelList.add(option1.getText().toString());
        levelList.add(option2.getText().toString());
        levelList.add(option3.getText().toString());
        levelList.add(option4.getText().toString());
        levelNumber=Integer.parseInt(level.getText().toString());

        //Log.d("hello",temp[4]);

        //Log.d("hello",levelList.get(0)[3]);

        question.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Delete entries?")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AddLevelActivity.super.onBackPressed();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}