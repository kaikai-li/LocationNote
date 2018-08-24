package com.lkk.locationnote.note.view;

import android.Manifest;
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
import com.lkk.locationnote.note.viewmodel.NoteListViewModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class NoteListFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = NoteListFragment.class.getSimpleName();
    private String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int REQUEST_PERMISSION_CODE = 0x301;

    @BindView(R2.id.note_recycler_view)
    RecyclerView mNoteRecyclerView;
    @BindView(R2.id.note_empty_view)
    TextView mEmptyView;

    private NoteRecyclerViewAdapter mAdapter;
    private NoteListViewModel mViewModel;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
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
        mViewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);
        mNoteRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mNoteRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new NoteRecyclerViewAdapter(getContext(), mViewModel, Collections.EMPTY_LIST);
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
        mViewModel.getOpenNoteEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer id) {
                Log.d(TAG, "Open note detail, id= " + id);
                NoteDetailActivity.start(getContext(), id);
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
                if (EasyPermissions.hasPermissions(getContext(), perms)) {
                    NoteAddEditActivity.start(getContext());
                } else {
                    EasyPermissions.requestPermissions(new PermissionRequest
                            .Builder(NoteListFragment.this, REQUEST_PERMISSION_CODE, perms)
                            .setRationale(R.string.location_rationale_text)
                            .setPositiveButtonText(R.string.ok)
                            .setTheme(R.style.alertDialogStype)
                            .build());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "On permissions granted");
        NoteAddEditActivity.start(getContext());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "On permissions denied");
    }
}
