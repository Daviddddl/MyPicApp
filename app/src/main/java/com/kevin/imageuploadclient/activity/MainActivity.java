package com.kevin.imageuploadclient.activity;

import android.support.v4.app.FragmentTransaction;

import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.basic.BaseActivity;
import com.kevin.imageuploadclient.fragment.MainFragment;
import com.kevin.imageuploadclient.fragment.basic.BaseFragment;


public class MainActivity extends BaseActivity {

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        initMainFragment();
    }

    /**
     * 初始化内容Fragment
     *
     * @return void
     */
    public void initMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment mFragment = MainFragment.newInstance();
        transaction.replace(R.id.main_act_container, mFragment, mFragment.getFragmentName());
        transaction.commit();
    }

    @Override
    protected void initEvents() {

    }
}
