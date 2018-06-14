package com.example.minhb.minder.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minhb.minder.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SelectWeatherActivity extends AppCompatActivity {

    String weatherAPIKey = "eae3cb3fed833e248275ac04b981464a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_weather);


    }




    private void requestReport(String cityName, String countryCode)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "," + countryCode + "&mode=json&APPID=" + weatherAPIKey;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Json Response: ", response);
                        try {
                            JSONObject weatherJson = new JSONObject(response);

                            /*TODO: (3)start adding weather forecast and condition for the next 5 days
                             * task item must have favorable condition,
                             * task list activity will have current/today weather condition*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();
    }
}
