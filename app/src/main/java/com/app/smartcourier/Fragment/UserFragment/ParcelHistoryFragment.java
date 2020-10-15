package com.app.smartcourier.Fragment.UserFragment;

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

public class ParcelHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    ParcelHistoryAdapter parcelHistoryAdapter;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    LinearLayoutManager layoutManager;
    //Adapter

    Context context;

    List<Parcel> parcelList;
    String TAG = getClass().getSimpleName(), contact = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_parcel_history,container,false);
        context = getContext();
        recyclerView = view.findViewById(R.id.historyParcelRv);
        sharedPreferences =context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        contact = sharedPreferences.getString(Config.CELL_SHARED_PREF, "Not Available");
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        getParcelData();
        return view;
    }

    private void getParcelData() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Parcel>> call;

            call = apiInterface.getParcelData(contact,"");


        Log.d(TAG, "onCreate: "+this);
        call.enqueue(new Callback<List<Parcel>>() {
            @Override
            public void onResponse(Call<List<Parcel>> call, Response<List<Parcel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    parcelList = response.body();
                    if (response.body().isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: "+response.body());
                        parcelHistoryAdapter = new ParcelHistoryAdapter(response.body(),context);
                        recyclerView.setAdapter(parcelHistoryAdapter);
                        parcelHistoryAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onResponse: "+parcelList.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Parcel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });

    }

}
