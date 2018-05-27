package com.lkk.locationnote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lkk.amap.MapFragment;

public class LocationNoteActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_MODE = "extra_current_mode";
    private static final int MODE_MAP = 1;
    private static final int MODE_NOTE = 2;

    private DrawerLayout mDrawerLayout;
    private Fragment mNoteFragment;
    private Fragment mMapFragment;
    private Fragment mCurrentFragment;
    private int mCurrentMode = MODE_MAP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_note);

        if (savedInstanceState != null) {
            mCurrentMode = savedInstanceState.getInt(EXTRA_CURRENT_MODE, MODE_MAP);
        }
        initView();
        uodateContentFragment();
    }

    private void initView() {
        initActionBar();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_entrance);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem switchItem = menu.findItem(R.id.fragment_content_switcher);
        switch (mCurrentMode) {
            case MODE_NOTE:
                switchItem.setIcon(R.drawable.ic_map);
                switchItem.setTitle(R.string.tab_map_title);
                break;
            case MODE_MAP:
                switchItem.setIcon(R.drawable.ic_event_note);
                switchItem.setTitle(R.string.tab_note_title);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.fragment_content_switcher:
                switch (mCurrentMode) {
                    case MODE_MAP:
                        mCurrentMode = MODE_NOTE;
                        break;
                    case MODE_NOTE:
                        mCurrentMode = MODE_MAP;
                        break;
                }
                uodateContentFragment();
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CURRENT_MODE, mCurrentMode);
    }

    private void uodateContentFragment() {
        switch (mCurrentMode) {
            case MODE_MAP:
                showMapFragment();
                break;
            case MODE_NOTE:
                showNoteFragment();
                break;
        }

        invalidateOptionsMenu();
    }

    private void showMapFragment() {
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
        }
        commitFragment(mCurrentFragment, mMapFragment);
    }

    private void showNoteFragment() {
        if (mNoteFragment == null) {
            mNoteFragment = NoteFragment.newInstance();
        }
        commitFragment(mCurrentFragment, mNoteFragment);
    }

    private void commitFragment(Fragment fragmentFrom, Fragment fragmentTo) {
        mCurrentFragment = fragmentTo;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragmentFrom == null) {
            transaction.replace(R.id.content_container, fragmentTo)
                    .commitAllowingStateLoss();
            return;
        }

        if (fragmentTo.isAdded()) {
            transaction.hide(fragmentFrom).show(fragmentTo)
                    .commitAllowingStateLoss();
        } else {
            transaction.hide(fragmentFrom)
                    .add(R.id.content_container, fragmentTo)
                    .commitAllowingStateLoss();
        }
    }
}
