package com.app.smartcourier.Activity.ManagerActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smartcourier.Activity.UserActivity.UserActivity;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Branch;
import com.app.smartcourier.Model.BranchManager;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangerProfileActivity extends AppCompatActivity {
    TextView textViewName, textViewEmail, textViewPassword,textViewContact,textViewBranch;
    EditText editTextRequest;
    Button buttonUpdate,buttonCancel;
    ProgressDialog progressDialog;
    String getContact;
    List<BranchManager> profileData;
    SharedPreferences sharedPreferences;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_profile);

        textViewName = findViewById(R.id.nameTv);
        textViewEmail = findViewById(R.id.emailTv);
        textViewPassword = findViewById(R.id.passwordTv);
        textViewContact = findViewById(R.id.contactTv);
        textViewBranch = findViewById(R.id.branchTv);
        editTextRequest = findViewById(R.id.requestEt);
        buttonUpdate = findViewById(R.id.updateBtn);
        buttonCancel = findViewById(R.id.cancelBtn);

        sharedPreferences =getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getContact = sharedPreferences.getString(Config.Manager_Cell_SHARED_PREF, "Not Available");

        getProfileData();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+profileData.toString());
                if (profileData!=null){
                    checkValidation();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MangerProfileActivity.this, UserActivity.class));
                finish();
            }
        });
    }

    private void checkValidation() {
//        if (textViewName.getText().toString().isEmpty()){
//            textViewName.setError("Name Can't be empty");
//            textViewName.requestFocus();
//        }
//        else if (textViewEmail.getText().toString().isEmpty()){
//            textViewEmail.setError("Email Can't be empty");
//            textViewEmail.requestFocus();
//        }
//        else if(textViewPassword.getText().toString().length()<4){
//            textViewPassword.setError("Password Can't be empty");
//            textViewPassword.requestFocus();
//        }
        if (!editTextRequest.getText().toString().isEmpty()){
            updateUser();
        }
//        else {
//            updateUser();
//        }

    }

    private void updateUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        Log.d(TAG, "signUp: "+ textViewEmail.getText().toString());
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<BranchManager> call = apiInterface.updateManager(
               editTextRequest.getText().toString()
        );

        call.enqueue(new Callback<BranchManager>() {
            @Override
            public void onResponse(Call<BranchManager> call, Response<BranchManager> response) {
                if (response.body().getValue().equals("success")){
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+response.body());
                    Toast.makeText(MangerProfileActivity.this, "Send Request Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(MangerProfileActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BranchManager> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(MangerProfileActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfileData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<BranchManager>> call;
        call = apiInterface.getManagerProfile(getContact);
        Log.d(TAG, "onCreate: "+getContact);
        call.enqueue(new Callback<List<BranchManager>>() {
            @Override
            public void onResponse(Call<List<BranchManager>> call, Response<List<BranchManager>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    profileData = response.body();
                    if (profileData.isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(MangerProfileActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        textViewName.setText(profileData.get(0).getName());
                        textViewContact.setText(profileData.get(0).getContact());
                        textViewPassword.setText(profileData.get(0).getPassword());
                        textViewBranch.setText(profileData.get(0).getBranchName());
                        textViewEmail.setText(profileData.get(0).getEmail());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<BranchManager>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MangerProfileActivity.this,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }
}
