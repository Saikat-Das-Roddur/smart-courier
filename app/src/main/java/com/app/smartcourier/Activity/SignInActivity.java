package com.app.smartcourier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smartcourier.Config;
import com.app.smartcourier.Model.BranchManager;
import com.app.smartcourier.Model.OtherInfo;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
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
    Call<List<User>> call;
    Call<List<BranchManager>> callManager;
    List<User> userData;
    List<BranchManager> managerData;

    String TAG = getClass().getSimpleName(), contact="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editTextContactNumber = findViewById(R.id.contactEt);
        editTextPassword = findViewById(R.id.passwordEt);
        textViewSignUp = findViewById(R.id.signUpTv);
        buttonSignIn = findViewById(R.id.signInbtn);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        call = apiInterface.getProfile(editTextContactNumber.getText().toString());
        callManager = apiInterface.getManagerProfile(editTextContactNumber.getText().toString());

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

        //getContactNo();


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
            signIn();
        }

    }

    private void getContactNo(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<OtherInfo>> call = apiInterface.getUserContact();
        Log.d(TAG, "getContactNo: "+call);
        call.enqueue(new Callback<List<OtherInfo>>() {
            @Override
            public void onResponse(Call<List<OtherInfo>> call, Response<List<OtherInfo>> response) {

                Log.d(TAG, "Response: "+response.body().toString());
            }

            @Override
            public void onFailure(Call<List<OtherInfo>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });


    }


    private void signIn() {
        //getProfileData();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
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
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
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
