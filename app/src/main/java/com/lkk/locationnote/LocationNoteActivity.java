package com.lkk.locationnote;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.lkk.amap.MapFragment;
import com.lkk.locationnote.common.BaseActivity;
import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.OnTitleRightIconCallback;
import com.lkk.locationnote.common.TitleView;

import butterknife.BindView;
import butterknife.OnPageChange;

public class LocationNoteActivity extends BaseActivity implements OnTitleRightIconCallback {

    @BindView(R.id.titleView)
    TitleView mTitleView;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_note);

        initView();
    }

    private void initView() {
        mTitleView.setTitle(R.string.app_name);
        ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
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

    private class ContentPagerAdapter extends FragmentPagerAdapter {

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
                    fragment = NoteFragment.newInstance();
                    break;
                case 2:
                    fragment = SettingsFragment.newInstance();
                    break;
            }
            if (fragment != null) {
                fragment.setRightIconCallback(LocationNoteActivity.this);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
