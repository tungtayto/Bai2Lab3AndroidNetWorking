package com.example.bai2lab3andoirdnetworking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String urlJsonObject = "http://192.168.1.91/kieuthanhtung_ph27675/person_object.json";
    private String urlJsonArr = "http://192.168.1.91/kieuthanhtung_ph27675/person_array.json";

    private static String TAG = MainActivity.class.getSimpleName();

    private Button btnObjectRequest;
    private Button btnArrRequest;
    private TextView tvResponse;

    private ProgressDialog progressDialog;
    private String jsonReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnObjectRequest = (Button) findViewById(R.id.btnObjectRequest);
        btnArrRequest = (Button) findViewById(R.id.btnArrRequest);
        tvResponse = (TextView) findViewById(R.id.tvResponse);

        btnArrRequest.setOnClickListener(this);
        btnObjectRequest.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting for you ... (Mono)");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnObjectRequest) {
            makeJsonObjectRequest();
        } else if (view.getId() == R.id.btnArrRequest) {
            makeJsonArrRequest();
        }
    }

    private void makeJsonArrRequest() {
        showDialog();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(urlJsonArr, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d(TAG, jsonArray.toString());
                try {
                    jsonReponse = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject person = (JSONObject) jsonArray.get(i);

                        String name = person.getString("name");
                        String email = person.getString("email");
                        JSONObject phone = person.getJSONObject("phone");
                        String home = phone.getString("home");
                        String mobile = phone.getString("mobile");

                        jsonReponse += "Name: " + name + "\n\n";
                        jsonReponse += "Email: " + email + "\n\n";
                        jsonReponse += "Home: " + home + "\n\n";
                        jsonReponse += "Mobile: " + mobile + "\n\n\n";
                    }

                    tvResponse.setText(jsonReponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(arrayRequest);
    }

    private void makeJsonObjectRequest() {
        showDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlJsonObject, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());
                try {

                    String name = jsonObject.getString("name");
                    String email = jsonObject.getString("email");
                    JSONObject phone = jsonObject.getJSONObject("phone");
                    String home = phone.getString("home");
                    String mobile = phone.getString("mobile");

                    jsonReponse = "";
                    jsonReponse += "Name: " + name + "\n\n";
                    jsonReponse += "Email: " + email + "\n\n";
                    jsonReponse += "Home: " + home + "\n\n";
                    jsonReponse += "Mobile: " + mobile + "\n\n";

                    tvResponse.setText(jsonReponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Hello", "Error: " + error.getMessage());
                Log.d("Hello",  "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}