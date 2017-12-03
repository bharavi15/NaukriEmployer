package com.bharavi.naukriemployer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
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

public class NewUser extends AppCompatActivity {

    EditText newUserEditText;
    EditText newPasswordEditText;
    Button newUserSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        newUserEditText=(EditText)findViewById(R.id.newUserEditText);
        newPasswordEditText=(EditText) findViewById(R.id.newPasswordEditText);
        newUserSubmitButton=(Button)findViewById(R.id.newUserSubmitButton);
        newUserSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://data.bluejay35.hasura-app.io/v1/query";

                try {
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    JSONObject jsonObject = new JSONObject()
                            .put("type", "insert")
                            .put("args", new JSONObject()
                                    .put("table", "Employer")
                                    .put("objects", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("EmployerID", null)
                                                    .put("Username", newUserEditText.getText().toString())
                                                    .put("Password", newPasswordEditText.getText().toString())
                                            )
                                    )
                            );
                    RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            //Handle failure
                        }

                        int affectedRows=0;
                        @Override
                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
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
                onBackPressed();
            }
        });

    }
}
