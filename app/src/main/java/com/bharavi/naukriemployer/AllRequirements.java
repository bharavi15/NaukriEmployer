package com.bharavi.naukriemployer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AllRequirements extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<JobModel> jobModelArrayList;
    int EmployerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_requirements);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        EmployerID=getIntent().getIntExtra("EmployerIDtoAllR",-1);
        String url = "https://data.bluejay35.hasura-app.io/v1/query";

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject()
                    .put("type", "select")
                    .put("args", new JSONObject()
                            .put("table", "RequirementsDB")
                            .put("columns", new JSONArray()
                                    .put("District")
                                    .put("Salary")
                                    .put("Vacancy")
                                    .put("TypeOfWork")
                            )
                            .put("where", new JSONObject()
                                    .put("EmployerID", new JSONObject()
                                            .put("$eq", "12345")
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
                    JSONObject result= new JSONObject();
                    try {
                        String res1=response.body().string();
                        JSONArray data=new JSONArray(res1);
                        Log.e("onResponse","res"+res1);
                        result=data.getJSONObject(0);


                        for(int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            JobModel job=new JobModel(object);
                            jobModelArrayList.add(job);
                        }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.plus, menu);
        } catch (Exception e) {
            Log.e("AllRequirements", "Couldnot Inflate");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent i = new Intent(this, AddRequirement.class);
        i.putExtra("EmployerIDtoAddR",EmployerID);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textView1;
            TextView textView2;
            TextView textView3;
            TextView textView4;
            public ViewHolder(View itemView) {
                super(itemView);
                textView1 = (TextView) itemView.findViewById(R.id.TOWTextView);
                textView2 = (TextView) itemView.findViewById(R.id.LocationTextView);
                textView3 = (TextView) itemView.findViewById(R.id.SalaryTextView);
                textView4 = (TextView) itemView.findViewById(R.id.VacancyTextView);
            }
        }
        @Override
        public JobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(JobsAdapter.ViewHolder holder, int position) {
            JobModel jobModel = jobModelArrayList.get(position);
            holder.textView1.setText(holder.textView1.getText()+jobModel.TypeOfWork);
            holder.textView2.setText(holder.textView2.getText()+jobModel.District);
            holder.textView3.setText(holder.textView3.getText()+String.valueOf(jobModel.Salary));
            holder.textView4.setText(holder.textView4.getText()+String.valueOf(jobModel.Vacancy));
        }

        @Override
        public int getItemCount() {
            return jobModelArrayList.size();
        }
    }
}

