package com.lkk.locationnote.note.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lkk.locationnote.common.BaseActivity;
import com.lkk.locationnote.common.utils.RouterPath;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.note.event.BackPressedEvent;
import com.lkk.locationnote.note.event.HideKeyboardEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = RouterPath.NOTE_ADD_EDIT_ACTIVITY)
public class NoteAddEditActivity extends BaseActivity {

    public static void start(Context context) {
        start(context, -1);
    }

    public static void start(Context context, @Nullable int noteId) {
        Intent intent = new Intent(context, NoteAddEditActivity.class);
        intent.putExtra(Util.EXTRA_NOTE_ID, noteId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NoteAddEditFragment fragment = (NoteAddEditFragment) getSupportFragmentManager()
                .findFragmentById(android.R.id.content);
        if (fragment == null) {
            fragment = NoteAddEditFragment.newInstance(getIntent().getIntExtra(Util.EXTRA_NOTE_ID, -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackPressedEvent(BackPressedEvent event) {
        onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hideSoftKeyboard(HideKeyboardEvent event) {
        hideSoftKeyboard();
    }
}
