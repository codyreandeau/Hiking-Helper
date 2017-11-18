package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class MountainSearch extends AppCompatActivity {

    private SearchView mountainName;
    private TextView mountainText, addressText, elevationText, difficultyText, parkingText, distanceText;
    String mountain, address, elevation, difficulty, parking, distance;
    private Button searchMountain;
    private RequestQueue requestQueue;
    private static final String URL = "https://hikinghelper.000webhostapp.com/connect/mountains_response.php";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mountain_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mountainName = (SearchView) findViewById(R.id.mountainSearch);
        searchMountain = (Button) findViewById(R.id.btnSearchMount);
        mountainText = (TextView) findViewById(R.id.txtMountainName);
        addressText = (TextView) findViewById(R.id.txtAddress);
        elevationText = (TextView) findViewById(R.id.txtElevation);
        difficultyText = (TextView) findViewById(R.id.txtDifficulty);
        parkingText = (TextView) findViewById(R.id.txtParking);
        distanceText = (TextView) findViewById(R.id.txtDistance);

        requestQueue = Volley.newRequestQueue(this);

        searchMountain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if any fields are left blank
                String strSearch = mountainName.getQuery().toString();
                if (strSearch.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Search field is blank.", Toast.LENGTH_SHORT).show();
                } else {
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

                                mountainText.setText(mountain);
                                addressText.setText(address);
                                elevationText.setText(elevation + "ft.");
                                difficultyText.setText(difficulty);
                                parkingText.setText(parking);
                                distanceText.setText(distance + " miles");

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
                            hashMap.put("mountainName", mountainName.getQuery().toString());
                            return hashMap;
                        }
                    };
                    requestQueue.add(request);
                }
            }
        });


        //set action bar text
    getSupportActionBar().setTitle("Mountain Search");

    //Send user back to the home page
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MountainSearch.this, Home.class));
        }
    });

    }

}
