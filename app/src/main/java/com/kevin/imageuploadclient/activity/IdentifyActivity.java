package com.kevin.imageuploadclient.activity;

import android.support.v4.app.FragmentTransaction;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.basic.BaseActivity;
import com.kevin.imageuploadclient.fragment.IdentifyFragment;
import com.kevin.imageuploadclient.fragment.basic.BaseFragment;

public class IdentifyActivity extends BaseActivity {

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_identify);
    }

    @Override
    protected void initViews() {
        initUploadFragment();
    }

    private void initUploadFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment mFragment = IdentifyFragment.newInstance();
        transaction.replace(R.id.identify_act_container, mFragment, mFragment.getFragmentName());
        transaction.commit();
    }

    @Override
    protected void initEvents() {

    }
}
