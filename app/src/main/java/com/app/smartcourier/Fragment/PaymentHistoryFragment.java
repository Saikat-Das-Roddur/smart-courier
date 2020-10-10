package com.app.smartcourier.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Activity.ProfileActivity;
import com.app.smartcourier.Adapter.PaymentHistoryAdapter;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.Model.Payment;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    PaymentHistoryAdapter paymentHistoryAdapter;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    LinearLayoutManager layoutManager;
    //ProgressDialog progressDialog;
    //Adapter

    List<Payment> paymentList;

    Context context;
    String TAG = getClass().getSimpleName(),contact = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_payment_history,container,false);
        context = getContext();
        recyclerView = view.findViewById(R.id.historyPaymentRv);

        sharedPreferences =context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        contact = sharedPreferences.getString(Config.CELL_SHARED_PREF, "Not Available");
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        getPaymentData();

        return view;
    }

    private void getPaymentData() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Payment>> call;
        call = apiInterface.getPaymentData(contact);
        Log.d(TAG, "onCreate: "+context);
        call.enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    paymentList = response.body();
                    if (response.body().isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: "+response.body());
                        paymentHistoryAdapter = new PaymentHistoryAdapter(context,response.body());
                        recyclerView.setAdapter(paymentHistoryAdapter);
                        paymentHistoryAdapter.notifyDataSetChanged();

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
                Toast.makeText(context,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });

    }
}
