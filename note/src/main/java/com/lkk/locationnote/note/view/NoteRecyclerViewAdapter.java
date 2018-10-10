package com.lkk.locationnote.note.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;
import com.lkk.locationnote.note.viewmodel.NoteListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteRecyclerViewAdapter extends
        Adapter<NoteRecyclerViewAdapter.NoteItemViewHolder> {

    private static final String TAG = NoteRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private NoteListViewModel mViewModel;
    private List<NoteEntity> mNotes;

    public NoteRecyclerViewAdapter(Context context, NoteListViewModel model,
                                   List<NoteEntity> notes) {
        mContext = context;
        mViewModel = model;
        mNotes = notes;
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.note_list_item_view, parent, false);
        return new NoteItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteItemViewHolder holder, int position) {
        NoteEntity note = mNotes.get(position);
        holder.mTitle.setText(note.getTitle());
        holder.mTime.setText(Util.getFormatTime(mContext, note.getTime()));
        holder.mContent.setText(note.getContent());
        holder.mLocation.setText(note.getLocation());
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    public void replaceData(List<NoteEntity> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    private int getNoteItemId(int position) {
        return mNotes.get(position).getId();
    }

    class NoteItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R2.id.note_item_location)
        TextView mLocation;
        @BindView(R2.id.note_item_title)
        TextView mTitle;
        @BindView(R2.id.note_item_time)
        TextView mTime;
        @BindView(R2.id.note_item_content)
        TextView mContent;

        public NoteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mViewModel.getOpenNoteEvent().setValue(getNoteItemId(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            mViewModel.getLongClickNoteEvent().setValue(getNoteItemId(getAdapterPosition()));
            return true;
        }
    }
}
