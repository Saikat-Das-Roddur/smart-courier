package com.app.smartcourier.Adapter.ManagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.smartcourier.Fragment.ManagerFragment.ManagerAddParcelFragment;
import com.app.smartcourier.Fragment.ManagerFragment.ManagerAddPaymentFragment;
import com.app.smartcourier.Fragment.ManagerFragment.ManagerParcelHistoryFragment;
import com.app.smartcourier.Fragment.ManagerFragment.ManagerPaymentHistoryFragment;

public class ManagerPaymentTabsAdapter extends FragmentPagerAdapter {
    int numberOfTabs;
    public ManagerPaymentTabsAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        String TAG = getClass().getSimpleName();


        switch (position){
            case 0:
                return new ManagerPaymentHistoryFragment();
            case 1:
                return new ManagerAddPaymentFragment();
            default:
                return new ManagerPaymentHistoryFragment();

        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
