package com.bharavi.naukriemployer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRequirement extends AppCompatActivity implements View.OnClickListener {

    EditText ageLimitEditText,districtEditText,salaryEditText,vacancyEditText,durationEditText,TOWEditText,fullAddressEditText;
    Button submitRequirement;
    int EmployerID,affectedRows=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_requirement);
        ageLimitEditText=findViewById(R.id.AgeLimit);
        districtEditText=findViewById(R.id.District);
        salaryEditText=findViewById(R.id.Salary);
        vacancyEditText=findViewById(R.id.Vacancy);
        durationEditText=findViewById(R.id.Duration);
        TOWEditText=findViewById(R.id.TypeOfWork);
        fullAddressEditText=findViewById(R.id.FullAddress);
        submitRequirement=findViewById(R.id.SubmitRequirement);
        submitRequirement.setOnClickListener(this);
        EmployerID=getIntent().getIntExtra("EmployerIDtoAddR",-1);
    }

    @Override
    public void onClick(View view) {
        String url = "https://data.bluejay35.hasura-app.io/v1/query";

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "insert")
                    .put("args", new JSONObject()
                            .put("table", "RequirementsDB")
                            .put("objects", new JSONArray()
                                    .put(new JSONObject()
                                            .put("Age", ageLimitEditText.getText().toString())
                                            .put("District", districtEditText.getText().toString().toLowerCase())
                                            .put("Salary", salaryEditText.getText().toString())
                                            .put("Vacancy", vacancyEditText.getText().toString())
                                            .put("TypeOfWork", TOWEditText.getText().toString().toLowerCase())
                                            .put("Duration", durationEditText.getText().toString())
                                            .put("FullAddress", fullAddressEditText.getText().toString().toLowerCase())
                                            .put("EmployerID", String.valueOf(EmployerID))
                                    )
                            )
                    );

            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            // If you have the auth token saved in shared prefs
            // SharedPreferences prefs = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
            // String authToken = prefs.getString("HASURA_AUTH_TOKEN", null);
            // You can now use this auth token in your header like so,
            // .addHeader(Authorization, "Bearer " + authToken);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer b6bfa6a8bd700af96efd53e712ba1ed3eb2d2e35321cbd40")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    //Handle failure
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    // Handle success
                    Log.e("NewUser","RESPONSE=");
                    JSONObject result= null;
                    try {
                        String res1=response.body().string();
                        Log.e("onResponse","res"+res1);
                        result = new JSONObject(res1);
                        affectedRows=result.getInt("affected_rows");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            // To execute the call synchronously
            // try {
            // 	Response response = client.newCall(request).execute();
            // 	String responseString = response.body().string(); // handle response
            // } catch (IOException e) {
            // 	e.printStackTrace(); // handle error
            // }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(affectedRows==-1)
            Toast.makeText(getApplicationContext(),"Requirement Added!",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"ERROR!",Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}

