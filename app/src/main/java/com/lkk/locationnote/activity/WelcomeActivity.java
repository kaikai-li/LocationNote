package com.lkk.locationnote.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lkk.locationnote.R;
import com.lkk.locationnote.common.BaseActivity;
import com.lkk.locationnote.common.utils.ImageLoaderUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class WelcomeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final int MSG_WHAT_NOTE = 0x300;
    private static final int REQUEST_PERMISSION_CODE = 0x301;
    private static final int SEND_MSG_DELAY = 3000;

    private String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static class WelcomeHandler extends Handler {
        private final WeakReference<WelcomeActivity> mActivity;

        public WelcomeHandler(WelcomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WelcomeActivity activity = mActivity.get();
            if (activity != null) {
                Intent intent = new Intent(activity, LocationNoteActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }

    private final WelcomeHandler mHandler = new WelcomeHandler(this);

    @BindView(R.id.welcome_image_view)
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        ImageLoaderUtil.loadImage(this, "http://api.dujin.org/bing/1920.php", mImageView);

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
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_WHAT_NOTE);
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
