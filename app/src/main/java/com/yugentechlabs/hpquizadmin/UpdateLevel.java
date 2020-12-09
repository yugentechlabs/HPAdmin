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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yugentechlabs.hpquizadmin.Adapters.MainAdapter;
import com.yugentechlabs.hpquizadmin.Model.Level;

import java.util.ArrayList;
import java.util.List;

public class UpdateLevel extends AppCompatActivity {


    int currentLevel;
    GridView gridView;
    static ArrayList<Level> levels;
    FirebaseFirestore db;
    ProgressDialog progress;
    public static final String TAG="HELLO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_level);

        levels=new ArrayList<Level>();
        db = FirebaseFirestore.getInstance();

        getLevels();

        //updateUI();

    }

    private void getLevels() {

        progress=new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.show();

        db.collection("WizardingQuizLevels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Level level=document.toObject(Level.class);
                                levels.add(level);
                            }
                            progress.dismiss();
                            updateUI();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void updateUI() {

        gridView=findViewById(R.id.grid_view);
        MainAdapter adapter= new MainAdapter(this,levels);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UpdateLevel.this, UpdateLevelIndividual.class);
                intent.putExtra("level", levels.get(position));
                startActivity(intent);
            }
        });

    }



}
