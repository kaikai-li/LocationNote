package com.lkk.locationnote.setting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.setting.BuildConfig;
import com.lkk.locationnote.setting.R;
import com.lkk.locationnote.setting.R2;

import butterknife.BindView;

public class SettingAboutFragment extends BaseFragment {

    public static final String TAG = SettingAboutFragment.class.getSimpleName();

    @BindView(R2.id.version_name)
    TextView mVersion;

    public static SettingAboutFragment newInstance() {
        return new SettingAboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVersion.setText(BuildConfig.VERSION_NAME);
    }
}
