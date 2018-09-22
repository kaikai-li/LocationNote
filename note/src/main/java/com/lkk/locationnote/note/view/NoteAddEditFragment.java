package com.lkk.locationnote.note.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.TitleView;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;
import com.lkk.locationnote.note.event.BackPressedEvent;
import com.lkk.locationnote.note.event.HideKeyboardEvent;
import com.lkk.locationnote.note.viewmodel.AddEditViewModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteAddEditFragment extends BaseFragment {

    @BindView(R2.id.add_edit_title)
    TitleView mTitleView;
    @BindView(R2.id.add_edit_content_title)
    TextInputEditText mTitle;
    @BindView(R2.id.add_edit_content)
    EditText mContent;
    @BindView(R2.id.add_edit_location)
    TextView mLocation;

    private int mNoteId;
    private AddEditViewModel mViewModel;
    private AddEditViewModel.NoteLocation mMapLocation;

    public static NoteAddEditFragment newInstance(int noteId) {
        NoteAddEditFragment fragment = new NoteAddEditFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitleView();
        mViewModel = ViewModelProviders.of(this).get(AddEditViewModel.class);
        mViewModel.getNote().observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                initView(noteEntity);
            }
        });
        mViewModel.getNoteUpdated().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                EventBus.getDefault().post(new BackPressedEvent());
            }
        });
        mViewModel.getLocation().observe(this, new Observer<AddEditViewModel.NoteLocation>() {
            @Override
            public void onChanged(@Nullable AddEditViewModel.NoteLocation aMapLocation) {
                mMapLocation = aMapLocation;
                if (aMapLocation != null && !TextUtils.isEmpty(mMapLocation.getAddress())) {
                    mLocation.setText(aMapLocation.getAddress());
                } else {
                    mLocation.setText(R.string.location_fail);
                }
            }
        });

        if (mNoteId < 0) {
            mViewModel.startLocation();
        }
    }

    @OnClick(R2.id.add_edit_location)
    public void location() {
        mViewModel.startLocation();
    }

    private void initTitleView() {
        if (mNoteId < 0) {
            mTitleView.setTitle(R.string.new_note_text);
        } else {
            mTitleView.setTitle(R.string.update_note_text);
        }
        mTitleView.setLeftIcon(R.drawable.ic_arrow_back);
        mTitleView.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BackPressedEvent());
            }
        });
        mTitleView.setLeftText(R.string.back);
        mTitleView.setRightText(R.string.add_note_done);
        mTitleView.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new HideKeyboardEvent());
                String title = mTitle.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    showSnackbar(getView(), R.string.note_title_empty);
                    return;
                }
                NoteEntity.Builder builder = new NoteEntity.Builder()
                            .title(title)
                            .content(mContent.getText().toString())
                            .time(System.currentTimeMillis());
                if (mMapLocation != null) {
                    builder.latitude(mMapLocation.getLatitude())
                            .longitude(mMapLocation.getLongitude());
                }
                if (mNoteId < 0) {
                    mViewModel.insertNote(builder.build());
                } else {
                    builder.id(mNoteId);
                    mViewModel.updateNote(builder.build());
                }
            }
        });
    }

    private void initView(NoteEntity noteEntity) {
        if (noteEntity == null) {
            return;
        }

        mTitle.setText(noteEntity.getTitle());
        mContent.setText(noteEntity.getContent());
        mViewModel.startRegeocode(new LatLonPoint(noteEntity.getLatitude(),
                noteEntity.getLongitude()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getNote(mNoteId);
    }
}
