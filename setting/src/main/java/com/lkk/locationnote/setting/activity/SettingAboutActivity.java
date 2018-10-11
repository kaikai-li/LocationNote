package com.lkk.locationnote.setting.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lkk.locationnote.common.BaseActivity;
import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.TitleView;
import com.lkk.locationnote.setting.R;
import com.lkk.locationnote.setting.R2;
import com.lkk.locationnote.setting.view.SettingAboutFragment;

import butterknife.BindView;

public class SettingAboutActivity extends BaseActivity {

    @BindView(R2.id.about_title)
    TitleView mTitleView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);
        initTitleView();
        BaseFragment aboutFragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentByTag(SettingAboutFragment.TAG);
        if (aboutFragment == null) {
            aboutFragment = SettingAboutFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.about_content, aboutFragment, SettingAboutFragment.TAG)
                    .commitAllowingStateLoss();
        }
    }

    private void initTitleView() {
        mTitleView.setTitle(R.string.about);
        mTitleView.setLeftIcon(R.drawable.ic_arrow_back);
        mTitleView.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
