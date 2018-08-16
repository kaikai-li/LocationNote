package com.lkk.locationnote.note.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;
import com.lkk.locationnote.note.viewmodel.NoteViewModel;

import java.util.ArrayList;

import butterknife.BindView;

public class NoteFragment extends BaseFragment {

    public static final String TAG = NoteFragment.class.getSimpleName();

    @BindView(R2.id.note_recycler_view)
    RecyclerView mNoteRecyclerView;

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
        mNoteRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mNoteRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new NoteRecyclerViewAdapter(new ArrayList<NoteEntity>(0));
        mNoteRecyclerView.setAdapter(mAdapter);
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
