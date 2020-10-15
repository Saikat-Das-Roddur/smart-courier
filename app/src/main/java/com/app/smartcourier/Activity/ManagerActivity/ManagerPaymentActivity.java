package com.app.smartcourier.Activity.ManagerActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.smartcourier.Adapter.ManagerAdapter.ManagerPaymentTabsAdapter;
import com.app.smartcourier.Adapter.UserAdapter.PaymentTabsAdapter;
import com.app.smartcourier.R;
import com.google.android.material.tabs.TabLayout;

public class ManagerPaymentActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener  {

    TabLayout tabLayout;
    ViewPager viewPager;

    String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPagerTab);

        tabLayout.addTab(tabLayout.newTab().setText("Payment History"));
        tabLayout.addTab(tabLayout.newTab().setText("Make Payment"));
        //tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ManagerPaymentTabsAdapter tabsAdapter = new ManagerPaymentTabsAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener( this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
