package com.lkk.locationnote.note.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lkk.locationnote.common.BaseActivity;
import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.utils.RouterPath;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.note.event.BackPressedEvent;
import com.lkk.locationnote.note.event.HideKeyboardEvent;
import com.lkk.locationnote.note.event.NoteEditEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = RouterPath.NOTE_DETAIL_ACTIVITY)
public class NoteDetailActivity extends BaseActivity {

    public static void start(Context context, int noteId) {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra(Util.EXTRA_NOTE_ID, noteId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(Util.EXTRA_NOTE_ID)) {
            int noteId = intent.getIntExtra(Util.EXTRA_NOTE_ID, -1);
            BaseFragment detailFragment = (NoteDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag(NoteDetailFragment.TAG);
            if (detailFragment == null) {
                detailFragment = NoteDetailFragment.newInstance(noteId);
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, detailFragment, NoteDetailFragment.TAG)
                        .commitAllowingStateLoss();
            }
        } else {
            finish();
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
    public void onNoteEditEvent(NoteEditEvent event) {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, NoteAddEditFragment.newInstance(event.getNoteId()))
                .addToBackStack(null)
                .commitAllowingStateLoss();
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
