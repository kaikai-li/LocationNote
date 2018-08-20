package com.lkk.locationnote.note.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;
import com.lkk.locationnote.note.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NoteFragment extends BaseFragment {

    public static final String TAG = NoteFragment.class.getSimpleName();

    @BindView(R2.id.note_recycler_view)
    RecyclerView mNoteRecyclerView;
    @BindView(R2.id.note_empty_view)
    TextView mEmptyView;

    private NoteRecyclerViewAdapter mAdapter;
    private NoteViewModel mViewModel;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTitle();
        mViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mNoteRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new NoteRecyclerViewAdapter(getContext(), new ArrayList<NoteEntity>(0));
        mNoteRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> noteEntities) {
                Log.d(TAG, "Notes changed");
                mAdapter.replaceData(noteEntities);
                setContentVisible(noteEntities == null || noteEntities.isEmpty());
            }
        });
    }

    private void setContentVisible(boolean empty) {
        if (empty) {
            mNoteRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mNoteRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadNotes();
    }

    private void initTitle() {
        setRightIconResId(R.drawable.ic_note_add);
        setRightIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
