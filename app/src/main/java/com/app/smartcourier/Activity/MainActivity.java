package com.app.smartcourier.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.smartcourier.R;

public class MainActivity extends AppCompatActivity {

    CardView cardViewTrack, cardViewPickUp,
            cardViewBranch,cardViewPayment,
            cardViewProfile, cardViewLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardViewTrack = findViewById(R.id.trackCard);
        cardViewPickUp = findViewById(R.id.pickUpCard);
        cardViewBranch = findViewById(R.id.branchCard);
        cardViewPayment = findViewById(R.id.paymentCard);
        cardViewProfile = findViewById(R.id.profileCard);
        cardViewLogout = findViewById(R.id.logoutCard);

        cardViewTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ParcelTrackActivity.class));
            }
        });
        cardViewPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ParcelPickActivity.class));
            }
        });
        cardViewBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BranchActivity.class));
            }
        });
        cardViewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PaymentActivity.class));
            }
        });
        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });
        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
