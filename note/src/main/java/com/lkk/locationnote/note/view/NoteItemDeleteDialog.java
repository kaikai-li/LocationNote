package com.lkk.locationnote.note.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;
import com.lkk.locationnote.note.event.ItemDeleteEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NoteItemDeleteDialog extends DialogFragment {

    private Unbinder mUnbinder;

    public static NoteItemDeleteDialog newInstance() {
        return new NoteItemDeleteDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_delete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R2.id.note_list_item_delete)
    public void onDeleteClick() {
        EventBus.getDefault().post(new ItemDeleteEvent());
        dismiss();
    }
}
