package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class preferences extends AppCompatActivity {

    private TextView mountainText, addressText, elevationText, difficultyText, parkingText, distanceText;
    private EditText maxMilesText, minMilesText, diffText;
    String mountain, address, elevation, difficulty, parking, distance;
    private Button searchMountain, tryAgain;
    private RequestQueue requestQueue;
    private static final String URL = "https://hikinghelper.000webhostapp.com/connect/preferences.php";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchMountain = (Button) findViewById(R.id.btnSearch);

        searchMountain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mountainResponse();
                searchMountain.setText("Try Again");
            }
        });

        getSupportActionBar().setTitle("Preferences Search");

        //Send User back to guide page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(preferences.this, MountainSearch.class));
            }
        });
    }

    private void mountainResponse() {
        mountainText = (TextView) findViewById(R.id.txtName);
        addressText = (TextView) findViewById(R.id.txtAddress);
        elevationText = (TextView) findViewById(R.id.txtElevation);
        difficultyText = (TextView) findViewById(R.id.txtDiff);
        parkingText = (TextView) findViewById(R.id.txtParking);
        distanceText = (TextView) findViewById(R.id.txtDistance);
        diffText = (EditText) findViewById(R.id.txtDifficulty);
        maxMilesText = (EditText) findViewById(R.id.txtMaxMiles);
        minMilesText = (EditText) findViewById(R.id.txtMinMiles);

        requestQueue = Volley.newRequestQueue(this);

        String strInput = diffText.getText().toString();

        if (strInput.equals("1") || strInput.equals("2") || strInput.equals("3") || strInput.equals("4") ||
                strInput.equals("5") || strInput.equals("6") || strInput.equals("7")) {
            //Validate user in the database
            request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Mountains");
                        JSONObject data = jsonArray.getJSONObject(0);

                        mountain = data.getString("mountainName");
                        address = data.getString("address");
                        elevation = data.getString("elevation");
                        difficulty = data.getString("dificulty");
                        parking = data.getString("parking");
                        distance = data.getString("length");

                        mountainText.setText("Name: " + mountain);
                        addressText.setText("Address: " + address);
                        elevationText.setText("Elevation: " + elevation + "ft.");
                        difficultyText.setText("Difficulty: " + difficulty);
                        parkingText.setText("Parking: " + parking);
                        distanceText.setText("Distance: " + distance + " miles");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Mountain Not Found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Mountain Not Found", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("length", minMilesText.getText().toString());
                    hashMap.put("length2", maxMilesText.getText().toString());
                    hashMap.put("dificulty", diffText.getText().toString());
                    return hashMap;
                }
            };
            requestQueue.add(request);
        }else{
            Toast.makeText(getApplicationContext(), "Difficulty must be between 1 and 7.", Toast.LENGTH_SHORT).show();
        }
    }
}