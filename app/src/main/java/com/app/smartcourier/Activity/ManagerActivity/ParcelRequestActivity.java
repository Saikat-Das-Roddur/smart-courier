package com.app.smartcourier.Activity.ManagerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.smartcourier.Adapter.ManagerAdapter.ParcelRequestAdapter;
import com.app.smartcourier.Adapter.UserAdapter.ParcelHistoryAdapter;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelRequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ParcelRequestAdapter parcelRequestAdapter;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    LinearLayoutManager layoutManager;
    List<Parcel> parcelList;
    String branch = "";
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_request);
        recyclerView = findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        branch = sharedPreferences.getString(Config.Branch_SHARED_PREF, "Not Available");
        Log.d(TAG, "branch: "+branch);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getParcelData();
    }

    private void getParcelData() {
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Parcel>> call;

        call = apiInterface.getBranchedParcel(branch);


        Log.d(TAG, "onCreate: "+this);
        call.enqueue(new Callback<List<Parcel>>() {
            @Override
            public void onResponse(Call<List<Parcel>> call, Response<List<Parcel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    parcelList = response.body();
                    if (response.body().isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ParcelRequestActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: "+response.body());
                        parcelRequestAdapter = new ParcelRequestAdapter(response.body(),ParcelRequestActivity.this);
                        recyclerView.setAdapter(parcelRequestAdapter);
                        parcelRequestAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onResponse: "+parcelList.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Parcel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ParcelRequestActivity.this,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }
}
