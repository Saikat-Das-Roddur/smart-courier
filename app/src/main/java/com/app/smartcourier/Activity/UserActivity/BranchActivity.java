package com.app.smartcourier.Activity.UserActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.app.smartcourier.Adapter.UserAdapter.BranchListAdapter;
import com.app.smartcourier.Model.Branch;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Branch> branches;
    BranchListAdapter branchListAdapter;
    LinearLayoutManager layoutManager;
    ProgressDialog progressDialog;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getBranches();
    }

    private void getBranches() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Branch>> call = apiInterface.getBranchData();

        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                Log.d(TAG, "onResponse: "+response.body());
                branchListAdapter = new BranchListAdapter(response.body(),BranchActivity.this);
                recyclerView.setAdapter(branchListAdapter);
                branchListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {

            }
        });
    }
}
