package com.yugentechlabs.hpquizadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

    EditText question, option1,option2, option3,option4;
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



        levelList=new ArrayList<String>();
        c=1;

        db = FirebaseFirestore.getInstance();

        getLevelfromDB();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(question.getText().toString().equals("") || option1.getText().toString().equals("") || option2.getText().toString().equals("") || option3.getText().toString().equals("") || option4.getText().toString().equals(""))) {
                    if (c < 10) {
                        c++;
                        nextPressed();
                    } else {
                        next.setEnabled(false);
                        submit.setEnabled(true);
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

        Level level=new Level(levelList,(levelNumber+1));
        progress=new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.show();

        db.collection("WizardingQuizLevels").document(String.valueOf(levelNumber+1)).set(level).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addLeveltoDB();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(AddLevelActivity.this, "Failed to add level.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddLevelActivity.this,MainActivity.class));
            }
        });


    }

    private void addLeveltoDB() {

        String l=String.valueOf(levelNumber+1);

        DocumentReference docRef = db.collection("WizardingQuizLevels").document("0");
        docRef.update("currentLevel",l).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress.dismiss();
                Toast.makeText(AddLevelActivity.this, "Level successfully added!", Toast.LENGTH_SHORT).show();
                AddLevelActivity.super.onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(AddLevelActivity.this, "Level successfully added!", Toast.LENGTH_SHORT).show();
                AddLevelActivity.super.onBackPressed();
            }
        });


    }


    private void getLevelfromDB() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();

        DocumentReference docRef = db.collection("WizardingQuizLevels").document("0");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        levelNumber=Integer.parseInt(document.getString("currentLevel"));
                        //Log.d(TAG, "DocumentSnapshot data: " + levelNumber);
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                startActivity(new Intent(AddLevelActivity.this,MainActivity.class));
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


        //Log.d("hello",temp[4]);

        //Log.d("hello",levelList.get(0)[3]);

        question.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
    }
}