package com.app.smartcourier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextName,editTextPassword,editTextEmail,editTextContactNumber;
    TextView textViewLogIn;
    Button buttonSignUp;

    String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        editTextName = findViewById(R.id.nameEt);
        editTextEmail = findViewById(R.id.emailEt);
        editTextPassword = findViewById(R.id.passwordEt);
        editTextContactNumber = findViewById(R.id.contactEt);
        textViewLogIn = findViewById(R.id.loginTv);
        buttonSignUp = findViewById(R.id.signUpbtn);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        textViewLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
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
        else if (editTextContactNumber.getText().toString().length()!=11 || !editTextContactNumber.getText().toString().startsWith("01")){
            editTextContactNumber.setError("Contact Can't be empty");
            editTextContactNumber.requestFocus();
        }
        else if(editTextPassword.getText().toString().length()<4){
            editTextPassword.setError("Password Can't be empty");
            editTextPassword.requestFocus();
        }
        else {
            signUp();
        }

    }

    private void signUp() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        Log.d(TAG, "signUp: "+ editTextEmail.getText().toString());
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.signUp(
                editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextContactNumber.getText().toString(),
                editTextPassword.getText().toString()
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getValue().equals("success")){
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+response.body());

                    startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(SignUpActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
