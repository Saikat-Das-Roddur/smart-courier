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

import com.app.smartcourier.Activity.ManagerActivity.ManagerActivity;
import com.app.smartcourier.Activity.UserActivity.UserActivity;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.BranchManager;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity {

    EditText editTextContactNumber, editTextPassword;
    TextView textViewSignUp;
    Button buttonSignIn;
    ProgressDialog progressDialog;

    ApiInterface apiInterface;

    String TAG = getClass().getSimpleName(), contact="",branch="";
    boolean isManager = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editTextContactNumber = findViewById(R.id.contactEt);
        editTextPassword = findViewById(R.id.passwordEt);
        textViewSignUp = findViewById(R.id.signUpTv);
        buttonSignIn = findViewById(R.id.signInbtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });




    }

    private void checkValidation() {
        if (editTextContactNumber.getText().toString().length()!=11 || !editTextContactNumber.getText().toString().startsWith("01")){
            editTextContactNumber.setError("Contact can't be empty");
            editTextContactNumber.requestFocus();
        }
        else if(editTextPassword.getText().toString().length()<4){
            editTextPassword.setError("Password can't be empty");
            editTextPassword.requestFocus();
        }
        else {
            getUserContactNo();
            getManagerContactNo();
            //signIn();
        }

    }

    private void getUserContactNo(){
        progressDialog.show();
        Call<List<User>> call = apiInterface.getProfile(editTextContactNumber.getText().toString());
        Log.d(TAG, "getContactNo: "+call);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body().size()>0){
                    isManager = false;

                    signIn();
                    Log.d(TAG, "Response: "+response.body().get(0).getContact());
                }
                else {
                    Log.d(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });


    }
    private void getManagerContactNo(){
        progressDialog.show();
        Call<List<BranchManager>> call = apiInterface.getManagerProfile(editTextContactNumber.getText().toString());
        Log.d(TAG, "getContactNo: "+call);
        call.enqueue(new Callback<List<BranchManager>>() {
            @Override
            public void onResponse(Call<List<BranchManager>> call, Response<List<BranchManager>> response) {
                if (response.body().size()>0){
                    isManager = true;
                    branch =response.body().get(0).getBranchName();
                    signIn();
                    Log.d(TAG, "Response: "+response.body());
                }

            }

            @Override
            public void onFailure(Call<List<BranchManager>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });


    }


    private void signIn() {
        //getProfileData();

        Log.d(TAG, "signIn: "+isManager);
        if (isManager){
            Call<BranchManager> call = apiInterface.managerSignIn(editTextContactNumber.getText().toString(), editTextPassword.getText().toString());

            Log.d(TAG, "Contact: "+contact+" Password: "+editTextPassword.getText().toString());

            call.enqueue(new Callback<BranchManager>() {
                @Override
                public void onResponse(Call<BranchManager> call, Response<BranchManager> response) {
                    if (response.body().getValue().equals("success")){
                        progressDialog.dismiss();
                        SharedPreferences sp = SignInActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sp.edit();
                        //Adding values to editor
                        editor.putString(Config.Branch_SHARED_PREF, branch);
                        editor.putString(Config.CELL_SHARED_PREF, editTextContactNumber.getText().toString());

                        //Saving values to editor
                        editor.commit();

                         Log.d(TAG, "You are manager "+branch);
                        startActivity(new Intent(SignInActivity.this, ManagerActivity.class));
                        finish();
                    }
                    else{
                        Log.d(TAG, "onResponse: "+call.request().body());
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BranchManager> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Call<User> call = apiInterface.signIn(editTextContactNumber.getText().toString(), editTextPassword.getText().toString());

            Log.d(TAG, "Contact: "+contact+" Password: "+editTextPassword.getText().toString());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body().getValue().equals("success")){
                        progressDialog.dismiss();
                        SharedPreferences sp = SignInActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sp.edit();
                        //Adding values to editor
                        editor.putString(Config.CELL_SHARED_PREF, editTextContactNumber.getText().toString());

                        //Saving values to editor
                        editor.commit();

                        // Log.d(TAG, "onResponse: "+isManager);
                        startActivity(new Intent(SignInActivity.this, UserActivity.class));
                        finish();
                    }
                    else{
                        Log.d(TAG, "onResponse: "+call.request().body());
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
