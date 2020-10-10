package com.app.smartcourier.Activity;

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

import com.app.smartcourier.Config;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword;
    TextView textViewContact;
    Button buttonUpdate,buttonCancel;
    ProgressDialog progressDialog;
    String getContact;
    List<User> profileData;
    SharedPreferences sharedPreferences;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextName = findViewById(R.id.nameEt);
        editTextEmail = findViewById(R.id.emailEt);
        editTextPassword = findViewById(R.id.passwordEt);
        textViewContact = findViewById(R.id.contactTv);
        buttonUpdate = findViewById(R.id.updateBtn);
        buttonCancel = findViewById(R.id.cancelBtn);

        sharedPreferences =getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getContact = sharedPreferences.getString(Config.CELL_SHARED_PREF, "Not Available");

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
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void checkValidation() {
        if (editTextName.getText().toString().isEmpty()){
            editTextName.setError("Name Can't be empty");
            editTextName.requestFocus();
        }
        else if (editTextEmail.getText().toString().isEmpty()){
            editTextEmail.setError("Email Can't be empty");
            editTextEmail.requestFocus();
        }
        else if(editTextPassword.getText().toString().length()<4){
            editTextPassword.setError("Password Can't be empty");
            editTextPassword.requestFocus();
        }
        else {
            updateUser();
        }

    }

    private void updateUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        Log.d(TAG, "signUp: "+ editTextEmail.getText().toString());
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.updateProfile(
                editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString()
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getValue().equals("success")){
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+response.body());
                    Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(ProfileActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfileData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<User>> call;
        call = apiInterface.getProfile(getContact);
        Log.d(TAG, "onCreate: "+getContact);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    profileData = response.body();
                    if (profileData.isEmpty()) {
                        //Toasty.warning(ProfileActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ProfileActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    } else {
                        editTextName.setText(profileData.get(0).getName());
                        textViewContact.setText(profileData.get(0).getContact());
                        editTextPassword.setText(profileData.get(0).getPassword());
                        editTextEmail.setText(profileData.get(0).getEmail());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this,"Error!"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }
}
