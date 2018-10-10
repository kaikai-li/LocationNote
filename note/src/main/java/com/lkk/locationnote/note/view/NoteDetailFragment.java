package com.lkk.locationnote.note.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.TitleView;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;
import com.lkk.locationnote.note.event.BackPressedEvent;
import com.lkk.locationnote.note.event.NoteEditEvent;
import com.lkk.locationnote.note.viewmodel.NoteDetailViewModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteDetailFragment extends BaseFragment {

    public static final String TAG = NoteDetailFragment.class.getSimpleName();

    @BindView(R2.id.note_detail_title_view)
    TitleView mTitleView;
    @BindView(R2.id.note_detail_title)
    TextView mTitle;
    @BindView(R2.id.note_detail_content)
    TextView mContent;
    @BindView(R2.id.note_detail_location)
    TextView mLocation;

    private int mNoteId;
    private NoteDetailViewModel mViewModel;
    private NoteEntity mNote;

    public static NoteDetailFragment newInstance(int noteId) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(Util.EXTRA_NOTE_ID, noteId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteId = getArguments().getInt(Util.EXTRA_NOTE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitleView();
        mViewModel = ViewModelProviders.of(this).get(NoteDetailViewModel.class);
        mViewModel.getNote().observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                Log.d(TAG, "Get note result= " + noteEntity);
                initView(noteEntity);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getNote(mNoteId);
    }

    @OnClick(R2.id.note_detail_location)
    public void locationClicked() {
        if (mNote.getLatitude() != 0 && mNote.getLongitude() != 0) {
            String location = String.format("geo:%f,%f", mNote.getLatitude(), mNote.getLongitude());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @OnClick(R2.id.note_detail_edit)
    public void editNote() {
        EventBus.getDefault().post(new NoteEditEvent(mNoteId));
    }

    private void initTitleView() {
        mTitleView.setTitle(R.string.note_detail_text);
        mTitleView.setLeftIcon(R.drawable.ic_arrow_back);
        mTitleView.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BackPressedEvent());
            }
        });
    }

    private void initView(NoteEntity noteEntity) {
        if (noteEntity == null) {
            return;
        }

        mNote = noteEntity;
        mTitle.setText(noteEntity.getTitle());
        mContent.setText(noteEntity.getContent());
        mLocation.setText(noteEntity.getLocation());
    }
}
