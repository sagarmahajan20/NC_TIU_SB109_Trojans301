package com.example.olx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Irrigation extends AppCompatActivity {

    private String ApiUrl = "https://sih-2020-1.herokuapp.com";
    private String temperature,ph,moisture;

    private TextView mtemp , mpHScale , mMoist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_irrigation);

       /* mtemp = findViewById(R.id.temperature);
        mpHScale = findViewById(R.id.pH);
        mMoist = findViewById(R.id.moisture);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ApiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    temperature = response.getString("temperature");
                    moisture = response.getString("moister");
                    ph = response.getString("pH");

                    mtemp.setText(temperature);
                    mpHScale.setText(ph);
                    mMoist.setText(moisture);

//                    Log.d("temp",temperature);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error","something went wrong"+error);
            }
        });
        requestQueue.add(objectRequest);*/
    }
}