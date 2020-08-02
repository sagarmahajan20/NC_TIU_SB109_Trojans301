package com.example.olx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
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

import java.io.IOException;
import java.net.URL;

public class Irrigation extends AppCompatActivity {

    private static final String TAG = Irrigation.class.getSimpleName();
    TextView tex1tView13;
    TextView textView13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_irrigation);

        tex1tView13 = findViewById(R.id.tex1tView13);
        textView13 = findViewById(R.id.textView13);
        URL parameterUrl = NetworkUtils.buildUrlForParameter();
        new FetchParameterDetails().execute(parameterUrl);
        Log.i(TAG, "onCreate: parameterUrl: " + parameterUrl);
    }
    private class FetchParameterDetails extends AsyncTask<URL, Void, String> {


        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL parameterUrl = urls[0];
            String parameterSearchResults = null;
            try {
                parameterSearchResults = NetworkUtils.getResponseFromHttpUrl(parameterUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "doInBackground: parameterSearchResults: " + parameterSearchResults);
            return parameterSearchResults;
        }

        @Override
        protected void onPostExecute(String parameterSearchResults) {

            super.onPostExecute(parameterSearchResults);
        }
    }
}