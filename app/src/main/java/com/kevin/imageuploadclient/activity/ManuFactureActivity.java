package com.kevin.imageuploadclient.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.basic.BaseActivity;
import com.kevin.imageuploadclient.fragment.ManufactureFragment;
import com.kevin.imageuploadclient.fragment.UploadFragment;
import com.kevin.imageuploadclient.fragment.basic.BaseFragment;

public class ManuFactureActivity extends BaseActivity {

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_manufacture);
    }

    @Override
    protected void initViews() {
        initUploadFragment();
    }

    private void initUploadFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment mFragment = ManufactureFragment.newInstance();
        transaction.replace(R.id.manu_act_container, mFragment, mFragment.getFragmentName());
        transaction.commit();
    }

    @Override
    protected void initEvents() {

    }
}
