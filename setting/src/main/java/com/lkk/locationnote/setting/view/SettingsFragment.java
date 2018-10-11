package com.lkk.locationnote.setting.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lkk.locationnote.common.BaseTabFragment;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.setting.R;
import com.lkk.locationnote.setting.R2;
import com.lkk.locationnote.setting.activity.SettingAboutActivity;

import java.io.File;

import butterknife.OnClick;

public class SettingsFragment extends BaseTabFragment {

    private static final String PROVIDER_AUTHORITIES = "com.lkk.locationnote.setting.provider";

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_settings, container, false);
    }

    @OnClick({R2.id.setting_feedback, R2.id.setting_about})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.setting_feedback) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final File targetFile = Util.zip(Util.getLogsDir(getContext()), "logs", false);
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            Uri targetUri;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                targetUri = FileProvider.getUriForFile(getContext(), PROVIDER_AUTHORITIES, targetFile);
                            } else {
                                targetUri = Uri.fromFile(targetFile);
                            }
                            startFeedbackSend(targetUri);
                        }
                    });
                }
            }).start();
        } else if (id == R.id.setting_about) {
            startActivity(new Intent(getContext(), SettingAboutActivity.class));
        }
    }

    private void startFeedbackSend(Uri attachment) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("application/x-zip-compressed");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"likaikai5566@126.com"});
        sendIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(sendIntent);
        } else {
            showSnackbar(getView(), R.string.feedback_hint);
        }
    }
}
