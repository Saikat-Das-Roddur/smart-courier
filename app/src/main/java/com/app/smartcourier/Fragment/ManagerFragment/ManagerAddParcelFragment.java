package com.app.smartcourier.Fragment.ManagerFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Branch;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerAddParcelFragment extends Fragment {
    EditText editTextTitle, editTextDesc,editTextLocation,editTextContact;
    TextView  textViewPayment, textViewBranch,textViewDestBranch,
            textViewCash, textViewBkash, textViewDate,textViewTime;
    Button buttonSubmit;
    RelativeLayout relativeLayoutBkash, relativeLayoutCash;

    Context context;
    SharedPreferences sharedPreferences;
    ArrayList<String> branches = new ArrayList<>();
    String TAG = getClass().getSimpleName(), branch = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_parcel_manager, container, false);
        context = getContext();
        editTextTitle = view.findViewById(R.id.titleEt);
        editTextDesc = view.findViewById(R.id.descEt);
        editTextContact = view.findViewById(R.id.contactEt);
        editTextContact = view.findViewById(R.id.contactEt);
        editTextLocation = view.findViewById(R.id.locationEt);
        textViewDate = view.findViewById(R.id.dateTv);
        textViewTime = view.findViewById(R.id.timeTv);
        textViewPayment = view.findViewById(R.id.paymentTv);
        textViewBranch = view.findViewById(R.id.branchTv);
        textViewDestBranch = view.findViewById(R.id.destBranchTv);
        buttonSubmit = view.findViewById(R.id.submitBtn);

        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        textViewDate.setText(formatter.format(today));
        textViewTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        Log.d(TAG, "onCreateView: " + UUID.randomUUID().toString().substring(27));
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        branch = sharedPreferences.getString(Config.Branch_SHARED_PREF, "branch");

        textViewBranch.setText(branch);

        textViewDestBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBranches();
            }
        });


        textViewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

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
        dialog.setCancelable(true);
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
                        if (!response.body().get(i).getBranchLocation().equalsIgnoreCase(branch)){
                            branches.add(response.body().get(i).getBranchLocation());
                        }
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Branch List");
                builder.setIcon(R.drawable.branch);

                builder.setItems(branches.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        textViewDestBranch.setText(branches.get(i));

                    }
                }).show();

            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {

            }
        });

    }

    private void checkValidation() {
        if (editTextTitle.getText().toString().isEmpty()) {
            editTextTitle.setError("Title can't be empty");
        } else if (editTextDesc.getText().toString().isEmpty()) {
            editTextDesc.setError("Title can't be empty");
        } else if (editTextLocation.getText().toString().isEmpty()) {
            editTextLocation.setError("Location can't be empty");
        } else if (editTextContact.getText().toString().length()!=11 && !editTextContact.getText().toString().startsWith("01")) {
            textViewPayment.setError("Set 11 digit contact");
        }else if (textViewPayment.getText().toString().isEmpty()) {
            textViewPayment.setError("Payment can't be empty");
        } else if (textViewBranch.getText().toString().isEmpty()) {
            textViewBranch.setError("Branch can't be empty");
        } else {
            submitParcel();
        }


    }
    private void submitParcel() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Parcel> call = apiInterface.managerSubmitParcel(
                UUID.randomUUID().toString().substring(27),
                editTextContact.getText().toString(),
                editTextTitle.getText().toString(),
                editTextDesc.getText().toString(),
                editTextLocation.getText().toString(),
                textViewPayment.getText().toString(),
                textViewBranch.getText().toString(),
                textViewDestBranch.getText().toString(),
                textViewTime.getText().toString(),
                textViewDate.getText().toString(),
                "Pending"
        );

        call.enqueue(new Callback<Parcel>() {
            @Override
            public void onResponse(Call<Parcel> call, Response<Parcel> response) {
                if (response.body().getValue().equals("success")) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: " + response.body());
                }

            }

            @Override
            public void onFailure(Call<Parcel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
//                Toast.makeText(SignUpActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





}
