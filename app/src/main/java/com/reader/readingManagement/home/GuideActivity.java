package com.reader.readingManagement.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.reader.readingManagement.R;

import java.util.ArrayList;

/**
 * Created by loll_ on 2017-05-18.
 */

public class GuideActivity extends Activity implements View.OnClickListener {

    private int mGuideIdx = 0;
    private ArrayList<LinearLayout> guides;
    private Button mCheckButton, mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_guide);

        guides = new ArrayList<>();
        guides.add((LinearLayout) findViewById(R.id.guide1));
        mCheckButton = (Button) findViewById(R.id.g_button_check);
        mCheckButton.setOnClickListener(this);
        mBackButton = (Button) findViewById(R.id.g_button_back);
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.g_button_check :
                guides.get(mGuideIdx++).setVisibility(View.GONE);
                if (mGuideIdx == guides.size()) finish();
                break;
            case R.id.g_button_back :
                if (mGuideIdx == 0) break;
                guides.get(--mGuideIdx).setVisibility(View.VISIBLE);
                break;
        }
    }
}
