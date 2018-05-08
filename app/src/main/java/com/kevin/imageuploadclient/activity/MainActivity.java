package com.kevin.imageuploadclient.activity;

import android.os.Bundle;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.basic.BaseActivity;
import com.kevin.imageuploadclient.fragment.MainFragment;
import com.kevin.imageuploadclient.fragment.basic.BaseFragment;


public class MainActivity extends BaseActivity {

    /*BottomNavigationView mBtnMain;
    MenuItem mBtnHelp;
    MenuItem mBtnMine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBtnHelp = findViewById(R.id.action_help);
        mBtnMain = findViewById(R.id.action_main);
        mBtnMine = findViewById(R.id.action_mine);

        mBtnMine.setActionView(

        );
    }*/

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
