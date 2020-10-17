package com.app.smartcourier.Activity.ManagerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.smartcourier.Activity.ParcelTrackActivity;
import com.app.smartcourier.Activity.SignInActivity;
import com.app.smartcourier.R;

public class ManagerActivity extends AppCompatActivity {
    CardView cardViewTrack, cardViewPickUp,
            cardViewBranch,cardViewPayment,
            cardViewProfile, cardViewLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


        cardViewTrack = findViewById(R.id.trackCard);
        cardViewPickUp = findViewById(R.id.pickUpCard);
        cardViewBranch = findViewById(R.id.branchCard);
        cardViewPayment = findViewById(R.id.paymentCard);
        cardViewProfile = findViewById(R.id.profileCard);
        cardViewLogout = findViewById(R.id.logoutCard);

        cardViewTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, ParcelTrackActivity.class));
            }
        });
        cardViewPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, ManagerParcelActivity.class));
            }
        });
        cardViewBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, ParcelRequestActivity.class));
            }
        });
        cardViewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, ManagerPaymentActivity.class));
            }
        });
        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, MangerProfileActivity.class));
            }
        });
        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}
