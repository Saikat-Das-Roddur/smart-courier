package com.app.smartcourier.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.smartcourier.Fragment.ParcelHistoryFragment;
import com.app.smartcourier.Fragment.ParcelSubmitFragment;

public class ParcelTabsAdapter extends FragmentPagerAdapter {
    int numberOfTabs;
    public ParcelTabsAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        String TAG = getClass().getSimpleName();


        switch (position){
            case 0:
                return new ParcelHistoryFragment();
            case 1:
                return new ParcelSubmitFragment();
            default:
                return new ParcelHistoryFragment();

        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
