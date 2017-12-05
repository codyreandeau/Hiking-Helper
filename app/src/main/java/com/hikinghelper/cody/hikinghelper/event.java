package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class event extends AppCompatActivity {

    private RequestQueue requestQueue;
    private static final String URL = "https://hikinghelper.000webhostapp.com/connect/fetch_events.php";
    private StringRequest request;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv=(ListView)findViewById(R.id.listv);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.content_listview, R.id.txt, items);
        lv.setAdapter(adapter);

        //set action bar text
        getSupportActionBar().setTitle("Events");

        //Set click event listener
        //button to send user to appropriate page
        Button btnGuide=(Button)findViewById(R.id.btnCreate);

        //Send user to create Event page
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(event.this, createEvent.class));
            }
        });

        //Send user back to the home page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(event.this, Home.class));
            }
        });
    }

    public void onStart() {
        super.onStart();
        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Events");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        items.add("Event: " + data.getString("name") + " - " + data.getString("location") + '\n' +
                                        "Date/Time: " + data.getString("date") + " - " + data.getString("time") + '\n' +
                                        "Description: " + data.getString("description") + '\n' +
                                        "Created By: " + data.getString("created_by"));
                                    }
                        } catch (JSONException e) {
                        e.printStackTrace();
                    } adapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
      requestQueue.add(request);
        }
}
