package com.app.smartcourier.Fragment.ManagerFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Branch;
import com.app.smartcourier.Model.Payment;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerAddPaymentFragment extends Fragment {

    EditText editTextTrackId,editTextBkashTrxId,editTextBkashNo,editTextAmount,editTextContact;
    TextView textViewBranch,textViewDate,textViewTime;
    Button buttonSubmit;
    SharedPreferences sharedPreferences;
    ArrayList<String> branches = new ArrayList<>();

    Context context;
    String TAG = getClass().getSimpleName(),branch = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_payment_manager,container,false);
        context = getContext();

        editTextTrackId = view.findViewById(R.id.trackIdEt);
        editTextBkashTrxId = view.findViewById(R.id.trxIdEt);
        editTextBkashNo = view.findViewById(R.id.bkashNoEt);
        editTextAmount = view.findViewById(R.id.bkashAmountEt);
        editTextContact = view.findViewById(R.id.contactEt);
        textViewBranch = view.findViewById(R.id.branchTv);
        textViewDate = view.findViewById(R.id.dateTv);
        textViewTime = view.findViewById(R.id.timeTv);
        buttonSubmit = view.findViewById(R.id.submitBtn);

        textViewTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        textViewDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        sharedPreferences =context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        branch = sharedPreferences.getString(Config.Branch_SHARED_PREF, "Not Available");

        textViewBranch.setText(branch);

        textViewBranch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showBranches();
                return true;
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

    private void showBranches() {
        branches.clear();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Branch>> call = apiInterface.getBranchData();

        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                Log.d(TAG, "onResponse: "+response.body());
                if (response.body()!=null){
                    for (int i = 0; i < response.body().size(); i++) {
                        branches.add(response.body().get(i).getBranchLocation());
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Member List");
                builder.setIcon(R.drawable.branch);

                builder.setItems(branches.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textViewBranch.setText(branches.get(i));

                    }
                }).show();

            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {

            }
        });

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
        Call<Payment> call = apiInterface.managerSubmitPayment(
                editTextTrackId.getText().toString(),
                editTextBkashTrxId.getText().toString(),
                editTextBkashNo.getText().toString(),
                editTextContact.getText().toString(),
                editTextAmount.getText().toString(),
                textViewBranch.getText().toString(),
                textViewDate.getText().toString(),
                textViewTime.getText().toString(),
                "Pending"
        );

        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.body().getValue().equals("success")){
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+response.body());
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
