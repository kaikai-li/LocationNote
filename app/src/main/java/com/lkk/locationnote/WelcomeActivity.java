package com.lkk.locationnote;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class WelcomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int MSG_WHAT_NOTE = 0x300;
    private static final int REQUEST_PERMISSION_CODE = 0x301;
    private static final int SEND_MSG_DELAY = 1000;

    private String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION};

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(WelcomeActivity.this, LocationNoteActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        if (EasyPermissions.hasPermissions(this, perms)) {
            mHandler.sendEmptyMessageDelayed(MSG_WHAT_NOTE, SEND_MSG_DELAY);
        } else {
            EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, REQUEST_PERMISSION_CODE, perms)
                    .setRationale(R.string.location_rationale_text)
                    .setPositiveButtonText(R.string.ok)
                    .setTheme(R.style.alertDialogStype)
                    .build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        mHandler.sendEmptyMessage(MSG_WHAT_NOTE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        mHandler.sendEmptyMessage(MSG_WHAT_NOTE);
    }
}
