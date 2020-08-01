package com.example.olx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

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
import java.util.List;

import adapter.HomeAdapter;
import model.HomeModel;
import model.HomeModelAnswer;
import model.QuestionFeed;

//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class ForumActivity extends AppCompatActivity {

    public static String currentUser = "UnqsZYCkMKX42nmvdtIW";


    private Toolbar toolbar;
    private RecyclerView feedRecyclerView;
    private RecyclerView.Adapter mDataAdapter;
    private ProgressBar progressBar;


    public String name,questionid;
    public HomeModel homeModel = new HomeModel();
    public List<HomeModel> entity = new ArrayList<>();
    public HomeModelAnswer homeModelAnswer = new HomeModelAnswer();
    public List<HomeModelAnswer> topVoteAns = new ArrayList<>();
    public List<String> answerId = new ArrayList<>();
    public List<String > userid = new ArrayList<>();


    //List to store all question that match perticular requirements
    List<QuestionFeed> questionList ;

    QuestionFeed questionFeed;

    List<String> idOfQuestions;

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



        feedRecyclerView = findViewById(R.id.recyclerview);


        //Fetching data in the main feedRecyclerView that is questions and answers
        //setting adapter and recyclerview


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedRecyclerView.setLayoutManager(layoutManager);
        mDataAdapter = new HomeAdapter(this,entity,topVoteAns,answerId,userid);

        feedRecyclerView.setAdapter(mDataAdapter);

        progressBar =  findViewById(R.id.progressBarMain1);
        progressBar.setVisibility(View.VISIBLE);

        readTags(new MyCallback() {
            @Override
            public void onCallBack(List<String> tags) {
                Log.d("callback", tags.toString());

                //Fetching all documents(question) where question tag == explore tag
                db.collectionGroup("question")
                        .whereArrayContainsAny("tags",Arrays.asList(tags.get(0), tags.get(1), tags.get(2)))

                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if(document.getBoolean("isanswered")==true){

                                    homeModel = document.toObject(HomeModel.class);
                                    Log.d("ting",document.getData().toString());
                                    entity.add(homeModel);

                                    String questionid = document.getId();
                                    Log.d("id",questionid);

                                    SharedPreferences preferences=getSharedPreferences("home",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putString("questionid",questionid);
                                    editor.commit();




//                                      HomeModel homeModel = document.toObject(HomeModel.class);
//                                      entity.add(homeModel);


//                                    homeModel.setCity(document.getString("city"));
//                                    homeModel.setName(document.getString("name"));
//                                    homeModel.setAnswer(document.getString("answer"));
//                                    homeModel.setQuestion(document.getString("question"));
//                                    homeModel.setUpvotes(document.getLong("upvotes").intValue());

                                    //mdatalist.add(homeModel);
//                                    List<String> nname = new ArrayList<>();
//                                    nname.add(name);
//                                    Intent intent = new Intent(ForumActivity.this,AddAnswer.class);
//                                    intent.putExtra("name",nname.toString());
//                                    startActivity(intent);
                                }

                            }
                            mDataAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("data2", "Error getting documents: ", task.getException());
                        }
                    }
                });


            }
        });

        SharedPreferences preferences=getSharedPreferences("home",MODE_PRIVATE);
        String n = preferences.getString("questionid","defaultValue");
        db.collectionGroup("answer").whereEqualTo("queId",n).orderBy("upvotes", Query.Direction.DESCENDING).limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("errorL", "Listen failed.", e);
                            return;
                        }
                        topVoteAns.clear();
                        answerId.clear();
                        userid.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.exists()) {
                                homeModelAnswer = doc.toObject(HomeModelAnswer.class);
                                Log.d("ans",doc.getData().toString());
                                topVoteAns.add(homeModelAnswer);
                                answerId.add(doc.getId());

                                String userId = doc.getReference().getParent().getParent().getId();
                                userid.add(userId);

                                doc.getReference().getParent().getParent().getId();
                            }
                        }
                        mDataAdapter.notifyDataSetChanged();
                    }
                });

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

        //Cloud function code

//        db.collectionGroup("question").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for(QueryDocumentSnapshot document :task.getResult()){
//                                if (document.getBoolean("IsAnswered") == true) {
//                                    idOfQuestions.add(document.getId());//getting Id's of all question document in the database.
//                                }
//                            }
//                        }
//                    }
//                });
//
//
//        for (String id : idOfQuestions) {
//
//            db.collectionGroup("answer").whereEqualTo("quesId", id).orderBy("upvotes", Query.Direction.DESCENDING).limit(1)
//                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            document.getString("answer");
//                            document.getString("upvotes");
//                            //Fetching personal data of the user who has got highest voted answer//
//                            document.getReference().getParent().getParent().get()
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()){
//                                        DocumentSnapshot document2 = task.getResult();
//                                        if (document2.exists()){
//                                            document2.getString("name");
//                                            document2.getString("city");
//                                            document2.getString("profile");
//                                        }
//                                    }
//                                }
//                            });
//
//                        }
//                    }
//
//                }
//            });
//
//        }
        //cloud function code


       /* Query question = db.collectionGroup("question");
        Query question_tag = question.whereArrayContains("Tags",Arrays.asList("a"));
         question_tag.whereArrayContains("IsAnswered",true).
                 get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d("data", document.getId() + " => " + document.getData());

                    }
                } else {
                    Log.d("data2", "Error getting documents: ", task.getException());
                }
            }
        });*/



        //Initialized and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        //Set home screen selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_answer:
                        startActivity(new Intent(getApplicationContext(), AddAnswer.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.my_content:
                        startActivity(new Intent(getApplicationContext(), MyContent.class));
                        overridePendingTransition(0,0);
                        return true;

//                    case R.id.notification:
//                        startActivity(new Intent(getApplicationContext(),Chatbot.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
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


    public void readTags(final MyCallback myCallback){

        //**HomeAdapter List data fecthing**//
        DocumentReference user = db.collection("Users").document(currentUser);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        List<String> explore ;
                        explore = (List<String>) document.get("explore");
                        myCallback.onCallBack(explore);
                    }
                }

            }
        });

    }


}