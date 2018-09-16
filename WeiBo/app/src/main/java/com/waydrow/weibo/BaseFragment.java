package com.waydrow.weibo;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by slomo on 2016/3/14.
 */
public class BaseFragment extends Fragment {
    protected MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
    }

}
