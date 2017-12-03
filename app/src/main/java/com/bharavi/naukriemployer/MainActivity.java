package com.bharavi.naukriemployer;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    int flag=0;
    Button submitButton;
    Button newUserButton;
    EditText username;
    EditText password;
    int count=0;
    int EmployerID=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitButton=(Button) findViewById(R.id.submitButton);
        newUserButton=(Button) findViewById(R.id.newUserButton);
        username=findViewById(R.id.usernameEditText);
        password=findViewById(R.id.passwordEditText);
    }

    public void onNewUser(View view) {
        Intent intent=new Intent(this,NewUser.class);
        startActivity(intent);
    }

    public void onSubmit(View view){
        String url = "https://data.bluejay35.hasura-app.io/v1/query";
        count++;
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "select")
                    .put("args", new JSONObject()
                            .put("table", "Employer")
                            .put("columns", new JSONArray()
                                    .put("EmployerID")
                            )
                            .put("where", new JSONObject()
                                    .put("$and", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Username", new JSONObject()
                                                            .put("$eq",username.getText().toString())
                                                    )
                                            )
                                            .put(new JSONObject()
                                                    .put("Password", new JSONObject()
                                                            .put("$eq", password.getText().toString())
                                                    )
                                            )
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

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    JSONObject result= new JSONObject();
                    try {
                        String res1=response.body().string();
                        JSONArray data=new JSONArray(res1);
                        Log.e("onResponse","res"+res1);
                        result=data.getJSONObject(0);

                        EmployerID=result.getInt("EmployerID");

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
        if(EmployerID!=-1) {
            Toast.makeText(getApplicationContext(), "Correct Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AllRequirements.class);
            intent.putExtra("EmployerIDtoAllR", EmployerID);
            startActivity(intent);
        }
        else
            if(count!=2)
            Toast.makeText(getApplicationContext(),"ERROR!",Toast.LENGTH_SHORT).show();
    }

}
