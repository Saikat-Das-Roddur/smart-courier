package com.app.smartcourier.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.smartcourier.Fragment.PaymentSubmitFragment;
import com.app.smartcourier.Fragment.PaymentHistoryFragment;


public class PaymentTabsAdapter extends FragmentPagerAdapter {
    int numberOfTabs;
    String TAG = getClass().getSimpleName();
    public PaymentTabsAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new PaymentHistoryFragment();
            case 1:
                return new PaymentSubmitFragment();
            default:
                return new PaymentHistoryFragment();

        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
