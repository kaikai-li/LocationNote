package com.lkk.locationnote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.lkk.amap.view.MapFragment;
import com.lkk.locationnote.R;
import com.lkk.locationnote.SwipeDisabledViewPager;
import com.lkk.locationnote.common.event.LocationServiceEvent;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.common.BaseActivity;
import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.OnTitleRightIconCallback;
import com.lkk.locationnote.common.TitleView;
import com.lkk.locationnote.note.view.NoteListFragment;
import com.lkk.locationnote.setting.view.SettingsFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnPageChange;

public class LocationNoteActivity extends BaseActivity implements OnTitleRightIconCallback {

    private static final String TAG = LocationNoteActivity.class.getSimpleName();
    private static final int REQUEST_CODE_LOCATION = 0x300;

    @BindView(R.id.titleView)
    TitleView mTitleView;
    @BindView(R.id.viewPager)
    SwipeDisabledViewPager mViewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_note);

        initView();
        if (!isLocationServiceEnabled()) {
            createAlertDialog(this, R.string.location_service_alert_msg)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(locationIntent, REQUEST_CODE_LOCATION);
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCATION) {
            EventBus.getDefault().post(new LocationServiceEvent());
        }
    }

    private void initView() {
        mTitleView.setTitle(R.string.app_name);
        mViewPager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager()));
        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mViewPager.setCurrentItem(item.getOrder());
                return true;
            }
        });
    }

    @OnPageChange(R.id.viewPager)
    public void onViewPagerChange(int position) {
        mBottomNavigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void setRightIcon(int resId) {
        mTitleView.setRightIcon(resId);
    }

    @Override
    public void setRightIconClickListener(View.OnClickListener listener) {
        mTitleView.setRightIconClickListener(listener);
    }

    public boolean isLocationServiceEnabled() {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Get location service error, " + e.getMessage());
            return false;
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    private static class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment = null;
            switch (position) {
                case 0:
                    fragment = MapFragment.newInstance();
                    break;
                case 1:
                    fragment = NoteListFragment.newInstance();
                    break;
                case 2:
                    fragment = SettingsFragment.newInstance();
                    break;
                default:
                        break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
