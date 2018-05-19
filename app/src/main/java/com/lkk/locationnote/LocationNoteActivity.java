package com.lkk.locationnote;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lkk.amap.MapFragment;

public class LocationNoteActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_note);

        initView();
    }

    private void initView() {
        initActionBar();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        mViewPager = findViewById(R.id.viewPager);
        NotePagerAdapter pagerAdapter = new NotePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_entrance);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class NotePagerAdapter extends FragmentPagerAdapter {

        private final int[] TAB_NAMES = new int[] {
                R.string.tab_map_title,
                R.string.tab_note_title
        };

        public NotePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getText(TAB_NAMES[position]);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = MapFragment.newInstance();
                    break;
                case 1:
                    fragment = NoteFragment.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_NAMES.length;
        }
    }
}
