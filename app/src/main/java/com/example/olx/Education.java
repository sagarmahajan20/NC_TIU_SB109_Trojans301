package com.example.olx;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapter.EducationAdapter;
import model.EducationModel;

public class Education extends AppCompatActivity {

    RecyclerView eduRecyclerView ;
    //    SearchView searchView;
//    Button filter;
    String filterName;

    //firebase obj
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EducationAdapter mDataAdapter;
    EducationModel educationModel;
    List<EducationModel> EducationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Schemes</font>"));
        setContentView(R.layout.activity_education);

        eduRecyclerView = findViewById(R.id.educationRecyclerView);
//        filter = findViewById(R.id.filterBtn);

//        searchView = findViewById(R.id.searchView);


        filterName = "filter";

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eduRecyclerView.setLayoutManager(layoutManager);
        mDataAdapter = new EducationAdapter(this,EducationList);
        eduRecyclerView.setAdapter(mDataAdapter);

        db.collection("Government Schemes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                educationModel = document.toObject(EducationModel.class);
                                EducationList.add(educationModel);
                                Log.d("Government Schemes", document.getId() + " => " + document.getData());
                            }
                            mDataAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Government Schemes", "Error getting documents: ", task.getException());
                        }
                    }
                });


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                String query2 = new String(query);
//
//                try {
//                    int income = Integer.parseInt(query);
//                    if (income <= 250000) {
//                        query2 = "less";
//                    } else if (income > 250000) {
//                        query2 = "greater";
//                    } else if (income <= 800000) {
//                        query2 = "medium";
//                    }
//
//                } catch (NumberFormatException nfe) {
//
//
//
//                }
//                db.collection("scheme").document("education").collection("postmatric_scheme").
//                        whereArrayContains("sort", query2.toLowerCase())
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    EducationList.clear();
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        educationModel = document.toObject(EducationModel.class);
//                                        EducationList.add(educationModel);
//                                        Log.d("kalyan", document.getId() + " => " + document.getData());
//                                    }
//                                    mDataAdapter.notifyDataSetChanged();
//                                } else {
//                                    Log.d("kalyan", "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
//
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
    }
}