package com.lkk.locationnote.note.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;

import com.lkk.locationnote.common.data.NoteEntity;

import java.util.List;

public class NoteRecyclerViewAdapter extends
        Adapter<NoteRecyclerViewAdapter.NoteItemViewHolder> {

    private List<NoteEntity> mNotes;

    public NoteRecyclerViewAdapter(List<NoteEntity> notes) {
        mNotes = notes;
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class NoteItemViewHolder extends RecyclerView.ViewHolder {

        public NoteItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
