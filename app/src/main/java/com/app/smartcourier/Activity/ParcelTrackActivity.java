package com.app.smartcourier.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.smartcourier.Adapter.ManagerAdapter.ManagerParcelHistoryAdapter;
import com.app.smartcourier.Adapter.ManagerAdapter.ManagerPaymentHistoryAdapter;
import com.app.smartcourier.Adapter.UserAdapter.ParcelHistoryAdapter;
import com.app.smartcourier.Adapter.UserAdapter.PaymentHistoryAdapter;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.Model.Payment;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelTrackActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressDialog progressDialog;
    //Adapter
    PaymentHistoryAdapter paymentHistoryAdapter;
    ParcelHistoryAdapter parcelHistoryAdapter;
    ManagerPaymentHistoryAdapter managerPaymentHistoryAdapter;
    ManagerParcelHistoryAdapter managerParcelHistoryAdapter;

    List<Payment> paymentList;
    List<Parcel> parcelList;


    EditText editTextSearch;
    ImageView imageViewFilter, imageViewSearch;
    RelativeLayout relativeLayoutParcel, relativeLayoutPayment;

    SharedPreferences sharedPreferences;
    boolean isParcel=false;
    String contact = "",branch = "",TAG = getClass().getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_track);
        editTextSearch = findViewById(R.id.searchEt);
        imageViewFilter = findViewById(R.id.filterIv);
        imageViewSearch = findViewById(R.id.searchIv);
        recyclerView = findViewById(R.id.recyclerView);
        sharedPreferences =getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        contact = sharedPreferences.getString(Config.CELL_SHARED_PREF, "Not Available");
        branch = sharedPreferences.getString(Config.Branch_SHARED_PREF, "Not Available");

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.d(TAG, "Contact: "+contact);
        Log.d(TAG, "Contact: "+branch);
        if (contact.equals(""))
            getBranchParcelData(branch);
        else
            getParcelData(contact);
        imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextSearch.getText().toString().isEmpty()){
                    if (isParcel){
                        getParcelData(editTextSearch.getText().toString());
                    }else
                        getPaymentData(editTextSearch.getText().toString());
                }

            }
        });

    }

    private void getBranchParcelData(String branch) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Parcel>> call;
        call = apiInterface.getManagerParcel(branch);


        Log.d(TAG, "onCreate: "+this);
        call.enqueue(new Callback<List<Parcel>>() {
            @Override
            public void onResponse(Call<List<Parcel>> call, Response<List<Parcel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    parcelList = response.body();
                    if (response.body().isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ParcelTrackActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: "+response.body());
                        managerParcelHistoryAdapter = new ManagerParcelHistoryAdapter(response.body(),ParcelTrackActivity.this);
                        recyclerView.setAdapter(managerParcelHistoryAdapter);
                        managerParcelHistoryAdapter.notifyDataSetChanged();

//                        Log.d(TAG, "onResponse: "+paymentList.toString());
//                        editTextName.setText(profileData.get(0).getName());
//                        textViewContact.setText(profileData.get(0).getContact());
//                        editTextPassword.setText(profileData.get(0).getPassword());
//                        editTextEmail.setText(profileData.get(0).getEmail());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Parcel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ParcelTrackActivity.this,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });

    }

    private void getPaymentData(String string) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Payment>> call;
        if (string.length()!=11){
            call = apiInterface.getPaymentData("",string);
        }else {
            call = apiInterface.getPaymentData(string,"");
        }

        Log.d(TAG, "onCreate: "+this);
        call.enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    paymentList = response.body();
                    if (response.body().isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ParcelTrackActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: "+response.body());
                        paymentHistoryAdapter = new PaymentHistoryAdapter(ParcelTrackActivity.this,response.body());
                        recyclerView.setAdapter(paymentHistoryAdapter);
                        paymentHistoryAdapter.notifyDataSetChanged();

                        Log.d(TAG, "onResponse: "+paymentList.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ParcelTrackActivity.this,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });

    }
    private void getParcelData(String string) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Parcel>> call;
        if (string.length()!=11){
            call = apiInterface.getParcelData("",string);
        }else {
            call = apiInterface.getParcelData(string,"");
        }

        Log.d(TAG, "onCreate: "+this);
        call.enqueue(new Callback<List<Parcel>>() {
            @Override
            public void onResponse(Call<List<Parcel>> call, Response<List<Parcel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    parcelList = response.body();
                    if (response.body().isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ParcelTrackActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: "+response.body());
                        parcelHistoryAdapter = new ParcelHistoryAdapter(response.body(),ParcelTrackActivity.this);
                        recyclerView.setAdapter(parcelHistoryAdapter);
                        parcelHistoryAdapter.notifyDataSetChanged();

                        Log.d(TAG, "onResponse: "+paymentList.toString());
//                        editTextName.setText(profileData.get(0).getName());
//                        textViewContact.setText(profileData.get(0).getContact());
//                        editTextPassword.setText(profileData.get(0).getPassword());
//                        editTextEmail.setText(profileData.get(0).getEmail());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Parcel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ParcelTrackActivity.this,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });

    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.track_dialog);
        relativeLayoutParcel = dialog.findViewById(R.id.parcelLayout);
        relativeLayoutPayment = dialog.findViewById(R.id.paymentLayout);

        relativeLayoutPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isParcel = false;
                if (contact.equals(""))
                  getBranchPaymentData(branch);
                else getPaymentData(contact);
            }
        });

        relativeLayoutParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isParcel = true;
                if (contact.equals(""))
                    getBranchParcelData(branch);
                else getParcelData(contact);
            }
        });

        dialog.show();
    }

    private void getBranchPaymentData(String branch) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Payment>> call;
        call = apiInterface.getManagerPayment(branch);


        Log.d(TAG, "onCreate: "+this);
        call.enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    paymentList = response.body();
                    if (response.body().isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ParcelTrackActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: "+response.body());
                        managerPaymentHistoryAdapter = new ManagerPaymentHistoryAdapter(ParcelTrackActivity.this,response.body());
                        recyclerView.setAdapter(managerPaymentHistoryAdapter);
                        managerPaymentHistoryAdapter.notifyDataSetChanged();

                        Log.d(TAG, "onResponse: "+paymentList.toString());
//                        editTextName.setText(profileData.get(0).getName());
//                        textViewContact.setText(profileData.get(0).getContact());
//                        editTextPassword.setText(profileData.get(0).getPassword());
//                        editTextEmail.setText(profileData.get(0).getEmail());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ParcelTrackActivity.this,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }
}
