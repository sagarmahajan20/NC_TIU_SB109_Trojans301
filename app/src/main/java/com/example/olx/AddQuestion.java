package com.example.olx;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class AddQuestion extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText write_question,tag_1,tag_2,tag_3 ;
    private Button send;
    private String question,tag1,tag2,tag3;
    ArrayList<String> tags = new ArrayList<>();
    private String YourApplicationID = "8806EZYTK6";
    private String YourAPIKey = "35cdf8c9d0de2b2f730864cd561861b5";
    private String my_index_name ="sih";
    //firebase database instances
    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Algolia search
    Client client = new Client(YourApplicationID, YourAPIKey);
    Index index = client.getIndex(my_index_name);

    private Map<String,Object> insertValues = new HashMap<>();
    private List<String> eligibleUsers = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




//        //toolbar
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Add Question");
//        toolbar.setLogo(R.drawable.account);

//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Add Question </font>"));

        setContentView(R.layout.activity_add_question);



        send = findViewById(R.id.add_que);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Initializing all views objects
                write_question = findViewById(R.id.addQuestion);
                tag_1 = findViewById(R.id.tag1);
                tag_2 = findViewById(R.id.tag2);
                tag_3 = findViewById(R.id.tag3);


                 question = write_question.getText().toString();
                 tag1= tag_1.getText().toString().toLowerCase();
                 tag2= tag_2.getText().toString().toLowerCase();
                 tag3= tag_3.getText().toString().toLowerCase();


                tags.add(tag1);
                tags.add(tag2);
                tags.add(tag3);

                //creating a map to add values.
//                insertValues.put("Question",question);
//                insertValues.put("Tags",tags);
//                insertValues.put("IsAnswered",false);
                //insertValues.put("timestamp", FieldValue.serverTimestamp());

                //Inserting values in the database.

                db.collection("Users").whereArrayContainsAny("interest", Arrays.asList(tag1.toLowerCase(),tag2.toLowerCase(),tag3.toLowerCase())).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        eligibleUsers.add(document.getId());
                                    }

                                    insertValues.put("question",question);
                                    insertValues.put("tags",tags);
                                    insertValues.put("isanswered",false);
                                    insertValues.put("eligibleUsers",eligibleUsers);
                                }




                              final String queId =  db.collection("Users").document(ForumActivity.currentUser).collection("question").document().getId();
                                            db.collection("Users").document(ForumActivity.currentUser).collection("question")
                                                    .document(queId).set(insertValues, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AddQuestion.this, "Question added Successfully", Toast.LENGTH_SHORT).show();
                                                //Adding data to algolia
                                                try {
                                                    index.addObjectAsync(new JSONObject()
                                                            .put("question", question)
                                                            .put("tag1", tag1)
                                                            .put("tag2", tag2)
                                                            .put("tag3", tag3)
                                                            .put("userid", ForumActivity.currentUser)
                                                            .put("questionid",queId), null);
                                                } catch (JSONException e) {
                                                    Toast.makeText(AddQuestion.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        });




            }
        });



    }

}
