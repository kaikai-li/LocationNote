package com.lkk.locationnote.note.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lkk.locationnote.common.BaseActivity;

public class NoteAddEditActivity extends BaseActivity {

    private static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    public static void start(Context context) {
        start(context, -1);
    }

    public static void start(Context context, @Nullable int noteId) {
        Intent intent = new Intent(context, NoteAddEditActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NoteAddEditFragment fragment = (NoteAddEditFragment) getSupportFragmentManager()
                .findFragmentById(android.R.id.content);
        if (fragment == null) {
            fragment = NoteAddEditFragment.getInstance(getIntent().getIntExtra(EXTRA_NOTE_ID, -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commitAllowingStateLoss();
        }
    }
}
