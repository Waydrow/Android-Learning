package com.waydrow.newloveinn.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.waydrow.newloveinn.fragment.EventFragment;
import com.waydrow.newloveinn.fragment.OrganizationFragment;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new EventFragment();
        } else {
            return new OrganizationFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return "公益活动";
        } else {
            return "公益机构";
        }
    }
}
