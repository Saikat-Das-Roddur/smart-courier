package com.app.smartcourier.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Activity.SignInActivity;
import com.app.smartcourier.Activity.SignUpActivity;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelSubmitFragment extends Fragment {

    EditText editTextTitle, editTextDesc;
    TextView textViewLocation, textViewPayment, textViewBranch,
            textViewCash, textViewBkash,textViewDate;
    Button buttonSubmit;
    RelativeLayout relativeLayoutBkash, relativeLayoutCash;

    //ProgressDialog progressDialog;
    //Adapter

    ArrayList<Parcel> parcelArrayList = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String TAG = getClass().getSimpleName(),trackingId="", contact="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_parcel_submit,container,false);
        context = getContext();
        editTextTitle = view.findViewById(R.id.titleEt);
        editTextDesc = view.findViewById(R.id.descEt);
        textViewLocation = view.findViewById(R.id.locationTv);
        textViewPayment = view.findViewById(R.id.paymentTv);
        textViewDate = view.findViewById(R.id.dateTv);
        textViewBranch = view.findViewById(R.id.branchTv);
        buttonSubmit = view.findViewById(R.id.submitBtn);

        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        textViewDate.setText(formatter.format(today));

        Log.d(TAG, "onCreateView: "+UUID.randomUUID().toString().substring(27));
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        contact = sharedPreferences.getString(Config.CELL_SHARED_PREF,"contact");

        textViewLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        textViewPayment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog();
                return false;
            }
        });
        textViewBranch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        return view;
    }

    private void showDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.payment_method_dialog);
        relativeLayoutBkash = dialog.findViewById(R.id.bkashLayout);
        relativeLayoutCash = dialog.findViewById(R.id.cashLayout);
        textViewBkash = dialog.findViewById(R.id.bkashTv);
        textViewCash = dialog.findViewById(R.id.cashTv);

        relativeLayoutBkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                textViewPayment.setText(textViewBkash.getText().toString());
            }
        });

        relativeLayoutCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                textViewPayment.setText(textViewCash.getText().toString());
            }
        });

        dialog.show();
    }

    private void checkValidation() {
        if (editTextTitle.getText().toString().isEmpty()){
            editTextTitle.setError("Title can't be empty");
        }
        else if (editTextDesc.getText().toString().isEmpty()){
            editTextTitle.setError("Title can't be empty");
        }
        else if (textViewLocation.getText().toString().isEmpty()){
            editTextTitle.setError("Location can't be empty");
        }
        else if (textViewPayment.getText().toString().isEmpty()){
            editTextTitle.setError("Payment can't be empty");
        }
        else if (textViewBranch.getText().toString().isEmpty()){
            editTextTitle.setError("Branch can't be empty");
        }else{
            submitParcel();
        }


    }

    private void submitParcel() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        //Log.d(TAG, "signUp: "+ editTextEmail.getText().toString());
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Parcel> call = apiInterface.submitParcel(
                UUID.randomUUID().toString().substring(27),
                contact,
                editTextTitle.getText().toString(),
                editTextDesc.getText().toString(),
                textViewLocation.getText().toString(),
                textViewPayment.getText().toString(),
                textViewBranch.getText().toString(),
                "",
                "",
                "pending"
        );

        call.enqueue(new Callback<Parcel>() {
            @Override
            public void onResponse(Call<Parcel> call, Response<Parcel> response) {
                if (response.body().getValue().equals("success")){
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+response.body());
//
//                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                    finish();
                }
                else{
//                    progressDialog.dismiss();
//                    Toast.makeText(SignUpActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Parcel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
//                Toast.makeText(SignUpActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
