package com.kevin.imageuploadclient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jkb.slidemenu.SlideMenuAction;
import com.kevin.imageuploadclient.R;

public class HelpActivity extends AppCompatActivity {

    private SlideMenuAction slideMenuAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        slideMenuAction = findViewById(R.id.mainSlideMenu);
        slideMenuAction.setSlideMode(SlideMenuAction.SLIDE_MODE_LEFT_RIGHT);
        slideMenuAction.setContentToggle(true);
    }
}
