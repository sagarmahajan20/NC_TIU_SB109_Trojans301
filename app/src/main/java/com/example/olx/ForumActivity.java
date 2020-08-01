package com.example.olx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.HomeAdapter;
import model.HomeModel;
//import model.HomeModelAnswer;
//import model.InitialDataModel;
import model.QuestionFeed;

//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class ForumActivity extends AppCompatActivity {

    public static String currentUser = "sagar";


    private Toolbar toolbar;
    private RecyclerView feedRecyclerView;
    private RecyclerView.Adapter mDataAdapter;
    private ProgressBar progressBar;
    private ImageView error;


    public String userId,questionid;
    public HomeModel homeModel = new HomeModel();
    public List<HomeModel> entity = new ArrayList<>();
//    public HomeModelAnswer homeModelAnswer = new HomeModelAnswer();
//    public List<HomeModelAnswer> topVoteAns = new ArrayList<>();
    public List<String> questionIdList = new ArrayList<>();
    public List<String > useridList = new ArrayList<>();
//    public List<InitialDataModel> initialDataList = new ArrayList<>();
//    public InitialDataModel initialDataModel = new InitialDataModel();
    public List<String > sharedPreferenceList = new ArrayList<>();
    public List<String > sharedPreferenceList2 = new ArrayList<>();


    //firebase obj
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseStorage storage = FirebaseStorage.getInstance();//getting instance of the firebase storage

    StorageReference storageRef = storage.getReference();// Create a storage reference from our app


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Home");
//        toolbar.setLogo(R.drawable.account);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Home </font>"));
        setContentView(R.layout.activity_forum);

        error = findViewById(R.id.error);

        feedRecyclerView = findViewById(R.id.recyclerview);


        //Fetching data in the main feedRecyclerView that is questions and answers
        //setting adapter and recyclerview


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedRecyclerView.setLayoutManager(layoutManager);
        mDataAdapter = new HomeAdapter(this,entity,questionIdList,useridList);

        feedRecyclerView.setAdapter(mDataAdapter);

        progressBar =  findViewById(R.id.progressBarMain1);
        progressBar.setVisibility(View.VISIBLE);


        db.collectionGroup("question").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("errorL", "Listen failed.", e);
                    return;
                }
                entity.clear();
                questionIdList.clear();
                useridList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        progressBar.setVisibility(View.INVISIBLE);
                        homeModel = doc.toObject(HomeModel.class);
                        entity.add(homeModel);

                        questionid = doc.getId();
                        questionIdList.add(questionid);

                        userId = doc.getReference().getParent().getParent().getId();
                        useridList.add(userId);

                }
                mDataAdapter.notifyDataSetChanged();
            }
        });


//        readTags(new MyCallback() {
//            @Override
//            public void onCallBack(List<String> tags) {
//                Log.d("callback", tags.toString());

//                //Fetching all documents(question) where question tag == explore tag
//                db.collectionGroup("question")
//                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            progressBar.setVisibility(View.INVISIBLE);
//
//                            for (QueryDocumentSnapshot document : task.getResult())
//                            {
//                                if(document.getBoolean("isanswered")==true){
//
//                                    homeModel = document.toObject(HomeModel.class);
//                                    Log.d("ting",document.getData().toString());
//                                    entity.add(homeModel);
//                                    String questionid = document.getId();
//                                    Log.d("id",questionid);
//
////                                    sharedPreferenceList.add(questionid);
//
//                                    db.collectionGroup("answer").whereEqualTo("queId",questionid)
//                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                                            if (e != null) {
//                                                                Log.w("errorL", "Listen failed.", e);
//                                                                return;
//                                                            }
////                                                    topVoteAns.clear();
////                                                    answerId.clear();
////                                                    userid.clear();
//                                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                                                        if (doc.exists()) {
//                                                            homeModelAnswer = doc.toObject(HomeModelAnswer.class);
//                                                            Log.d("ans", doc.getData().toString());
//                                                            topVoteAns.add(homeModelAnswer);
//
//                                                            Log.d("testing","testing");
//                                                            answerId.add(doc.getId());
//
//                                                            String userId = doc.getReference().getParent().getParent().getId();
//                                                            userid.add(userId);
//
//                                                            //getting name of answer user
////                                    doc.getReference().getParent().getParent().getId();
//                                                        }
//                                                        else {
//                                                            Log.d("tempError","error 404");
//                                                        }
//                                                    }
//                                                }
//                                            });
//
////                                    SharedPreferences preferences=getSharedPreferences("home",MODE_PRIVATE);
////                                    SharedPreferences.Editor editor=preferences.edit();
////                                    editor.putString(questionid,questionid);
////                                    editor.commit();
//
//
//                                }
////                                else{
////
//////                                    db.collectionGroup("fakeData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//////                                        @Override
//////                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//////                                            if (task.isSuccessful()) {
//////                                                progressBar.setVisibility(View.INVISIBLE);
//////                                                for (QueryDocumentSnapshot document : task.getResult()){
//////                                                    if (document.exists()){
//////                                                        initialDataModel = document.toObject(InitialDataModel.class);
//////                                                        initialDataList.add(initialDataModel);
//////                                                    }
//////                                                }
//////
//////                                            }
//////                                        }
//////                                    });
//////                                    error.setVisibility(View.VISIBLE);
//////                                    progressBar.setVisibility(View.INVISIBLE);
////                                    Toast.makeText(ForumActivity.this, "Sorry! Server Error", Toast.LENGTH_SHORT).show();
////
////                                }
//
//                            }
//                            Log.d("q",entity.toString());
//                            Log.d("top", topVoteAns.toString());
//                            mDataAdapter.notifyDataSetChanged();
////                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ForumActivity.this);
////                            SharedPreferences.Editor mEdit1 = sp.edit();
////
////                            mEdit1.putInt("Status_size", sharedPreferenceList.size());
//
////                            for(int i=0;i<sharedPreferenceList.size();i++)
////                            {
////                                mEdit1.remove("Status_" + i);
////                                mEdit1.putString("Status_" + i, sharedPreferenceList.get(i));
////                            }
////                            mEdit1.commit();
////                            Log.d("idl",sharedPreferenceList.toString());
//
//
//                        } else {
//                            Log.d("data2", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });


//            }
//        });

//        SharedPreferences preferences=getSharedPreferences("home",MODE_PRIVATE);
//        String n = preferences.getString("questionid","defaultValue");

//        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(ForumActivity.this);

//        int size = mSharedPreference1.getInt("Status_size", 0);

//        for(int i=0;i<size;i++) {
//            sharedPreferenceList.add(mSharedPreference1.getString("Status_" + i, null));
//            Log.d("nh", String.valueOf(size));
//
////        Log.d("n",n);
////        if(n == "defaultValue")
////        {
//////            error.setVisibility(View.VISIBLE);
//////            progressBar.setVisibility(View.INVISIBLE);
////            Toast.makeText(ForumActivity.this, "Sorry! Server Error", Toast.LENGTH_SHORT).show();
////
////        }
////        else {
//            Log.d("nhq",mSharedPreference1.getString("Status_" + i, null));
//            db.collectionGroup("answer").whereEqualTo("queId", mSharedPreference1.getString("Status_" + i, null)).orderBy("upvotes", Query.Direction.DESCENDING).limit(1)
//                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot value,
//                                            @Nullable FirebaseFirestoreException e) {
//                            if (e != null) {
//                                Log.w("errorL", "Listen failed.", e);
//                                return;
//                            }
//                            topVoteAns.clear();
//                            answerId.clear();
//                            userid.clear();
//                            for (QueryDocumentSnapshot doc : value) {
//                                if (doc.exists()) {
//                                    homeModelAnswer = doc.toObject(HomeModelAnswer.class);
//                                    Log.d("ans", doc.getData().toString());
//                                    topVoteAns.add(homeModelAnswer);
//
//                                    answerId.add(doc.getId());
//
//                                    String userId = doc.getReference().getParent().getParent().getId();
//                                    userid.add(userId);
//
//                                    //getting name of answer user
////                                    doc.getReference().getParent().getParent().getId();
//                                }
//                            }
//                            Log.d("top", topVoteAns.toString());
//
//
//                        }
//                    });
//        }
//        Log.d("top1", answerId.toString());
//        mDataAdapter.notifyDataSetChanged();
////        }
//
//
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        homeModelAnswer = document.toObject(HomeModelAnswer.class);
//                        Log.d("ans",document.getData().toString());
//                        topVoteAns.add(homeModelAnswer);
//                    }
//                    mDataAdapter.notifyDataSetChanged();
//
//                }
//                else
//                {      Log.d("ans",task.getException().toString());
//                }
//            }
//        });






                                    //Initialized and assign variable
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        //Set home screen selected
//        bottomNavigationView.setSelectedItemId(R.id.home);

//        //Perform ItemSelectListener
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.add_answer:
//                        startActivity(new Intent(getApplicationContext(), AddAnswer.class));
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.my_content:
//                        startActivity(new Intent(getApplicationContext(), MyContent.class));
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.home:
//                        return true;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.search:
                startActivity(new Intent(getApplicationContext(),Search.class));
                overridePendingTransition(0,0);
                return true;

            case R.id.ask:
                startActivity(new Intent(getApplicationContext(),AddQuestion.class));
                overridePendingTransition(0,0);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


//    public void readTags(final MyCallback myCallback){
//
//        //**HomeAdapter List data fecthing**//
//        DocumentReference user = db.collection("Users").document(currentUser);
//
//        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()){
//                        List<String> explore ;
//                        explore = (List<String>) document.get("explore");
//                        myCallback.onCallBack(explore);
//                    }
//                }
//
//            }
//        });
//
//    }


}