package com.yugentechlabs.hpquizadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class UpdateLevelIndividual extends AppCompatActivity {

    EditText question, option1,option2, option3,option4;
    Button next, submit;
    TextView quesNum;
    ArrayList<String> levelList;
    int c,levelNumber;
    FirebaseFirestore db;
    ProgressDialog progress;
    public static final String TAG="fire";
    int i;
    Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_level_individual);
        i=0;

        level= (Level) getIntent().getSerializableExtra("level");

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

        //getLevelfromDB();

        showLevels();

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
                    Toast.makeText(UpdateLevelIndividual.this, "Fill the details", Toast.LENGTH_SHORT).show();
                }
            }});


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(question.getText().toString().equals("") || option1.getText().toString().equals("") || option2.getText().toString().equals("") || option3.getText().toString().equals("") || option4.getText().toString().equals(""))) {
                    submitPressed();
                }
                else{
                    Toast.makeText(UpdateLevelIndividual.this, "Fill the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showLevels() {
        question.setText(level.getLevel().get(i++));
        option1.setText(level.getLevel().get(i++));
        option2.setText(level.getLevel().get(i++));
        option3.setText(level.getLevel().get(i++));
        option4.setText(level.getLevel().get(i++));
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

        addLeveltoDB();
    }



    private void addLeveltoDB() {

        Level l=new Level(levelList,level.getLevelnum());

        db.collection("WizardingQuizLevels").document(String.valueOf(level.getLevelnum())).set(l).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress.dismiss();
                Toast.makeText(UpdateLevelIndividual.this, "Level Updated successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateLevelIndividual.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(UpdateLevelIndividual.this, "Failed to add level.", Toast.LENGTH_SHORT).show();

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

        question.setText(level.getLevel().get(i++));
        option1.setText(level.getLevel().get(i++));
        option2.setText(level.getLevel().get(i++));
        option3.setText(level.getLevel().get(i++));
        option4.setText(level.getLevel().get(i++));
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel update?")
                .setMessage("Are you sure you want to cancel the update?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateLevelIndividual.super.onBackPressed();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}