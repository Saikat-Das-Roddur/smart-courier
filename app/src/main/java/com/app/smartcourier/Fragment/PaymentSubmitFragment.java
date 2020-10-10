package com.app.smartcourier.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.smartcourier.Activity.SignUpActivity;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.Model.Payment;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSubmitFragment extends Fragment {

    EditText editTextTrackId,editTextBkashTrxId,editTextBkashNo,editTextAmount;
    TextView textViewBranch;
    Button buttonSubmit;
    SharedPreferences sharedPreferences;

    Context context;
    String TAG = getClass().getSimpleName(),contact = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_make_payment,container,false);
        context = getContext();

        editTextTrackId = view.findViewById(R.id.trackIdEt);
        editTextBkashTrxId = view.findViewById(R.id.trackIdEt);
        editTextBkashNo = view.findViewById(R.id.bkashNoEt);
        editTextAmount = view.findViewById(R.id.bkashAmountEt);
        textViewBranch = view.findViewById(R.id.branchTv);
        buttonSubmit = view.findViewById(R.id.submitBtn);

        sharedPreferences =context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        contact = sharedPreferences.getString(Config.CELL_SHARED_PREF, "Not Available");

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

    private void checkValidation() {
        if (editTextTrackId.getText().toString().isEmpty()){
            editTextTrackId.setError("Tracking ID can't be empty");
        }
        else if (editTextBkashTrxId.getText().toString().isEmpty()){
            editTextBkashTrxId.setError("Bkash Trx ID can't be empty");
        }
        else if (editTextBkashNo.getText().toString().isEmpty()){
            editTextBkashNo.setError("Bkash No can't be empty");
        }
//        else if (editTextBkashNo.getText().toString().length()!=11 || editTextBkashNo.getText().toString().startsWith("01") ){
//            editTextBkashNo.setError("Please input number with 11 digit which starts with 01");
//        }

        else if (editTextAmount.getText().toString().isEmpty()){
            editTextAmount.setError("Amount can't be empty");
        }
        else if (textViewBranch.getText().toString().isEmpty()){
            textViewBranch.setError("Branch can't be empty");
        }else {
            submitPayment();
        }

    }

    private void submitPayment() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        //Log.d(TAG, "signUp: "+ editTextEmail.getText().toString());
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Payment> call = apiInterface.submitPayment(
                editTextTrackId.getText().toString(),
                editTextBkashTrxId.getText().toString(),
                editTextBkashNo.getText().toString(),
                contact,
                editTextAmount.getText().toString(),
                textViewBranch.getText().toString(),
                "Pending"
        );

        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.body().getValue().equals("success")){
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+response.body());
//
//                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(context, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
