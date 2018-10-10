package com.lkk.locationnote.note.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lkk.locationnote.common.BaseFragment;
import com.lkk.locationnote.common.TitleView;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;
import com.lkk.locationnote.note.event.BackPressedEvent;
import com.lkk.locationnote.note.event.HideKeyboardEvent;
import com.lkk.locationnote.note.viewmodel.AddEditViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteAddEditFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = NoteAddEditFragment.class.getSimpleName();
    private static final int REQUEST_CODE_ADD_LOCATION = 0x300;

    @BindView(R2.id.add_edit_title)
    TitleView mTitleView;
    @BindView(R2.id.add_edit_content_title)
    TextInputEditText mTitle;
    @BindView(R2.id.add_edit_content)
    EditText mContent;
    @BindView(R2.id.add_edit_location)
    TextView mLocation;
    @BindView(R2.id.add_edit_location_spinner)
    Spinner mAddressSpinner;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleView();
        mAddressSpinner.setOnItemSelectedListener(this);
    }

    private void replaceSpinnerData(List<String> addresses) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, addresses);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAddressSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddEditViewModel.class);
        mViewModel.getNote().observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                initViewFromEntity(noteEntity);
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
            public void onChanged(@Nullable AddEditViewModel.NoteLocation mapLocation) {
                Log.d(TAG, "On location changed, mapLocation= " + mapLocation);
                mMapLocation = mapLocation;
                boolean locationSuccess = mapLocation != null && !mapLocation.getAddresses().isEmpty();
                updateLocationViewVisible(locationSuccess);
                if (locationSuccess) {
                    List<String> addresses = mapLocation.getAddresses();
                    mLocation.setText(addresses.get(0));
                    addresses.add(getResources().getString(R.string.add_location));
                    replaceSpinnerData(addresses);
                } else {
                    mLocation.setText(R.string.location_fail);
                }
            }
        });

        if (mNoteId < 0) {
            mViewModel.startLocation();
        }
    }

    private void updateLocationViewVisible(boolean spinnerVisible) {
        mAddressSpinner.setVisibility(spinnerVisible ? View.VISIBLE : View.GONE);
        mLocation.setVisibility(spinnerVisible ? View.GONE : View.VISIBLE);
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
        mTitleView.setRightText(R.string.done);
        mTitleView.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new HideKeyboardEvent());
                String title = mTitle.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    showSnackbar(getView(), R.string.note_title_empty);
                    return;
                }
                if (getResources().getString(R.string.add_location)
                        .equals(mLocation.getText().toString())) {
                    showSnackbar(getView(), R.string.please_select_location);
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
                builder.location(mLocation.getText().toString());
                if (mNoteId < 0) {
                    mViewModel.insertNote(builder.build());
                } else {
                    builder.id(mNoteId);
                    mViewModel.updateNote(builder.build());
                }
            }
        });
    }

    private void initViewFromEntity(NoteEntity noteEntity) {
        if (noteEntity == null) {
            return;
        }

        mTitle.setText(noteEntity.getTitle());
        mContent.setText(noteEntity.getContent());
        mLocation.setText(noteEntity.getLocation());
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getNote(mNoteId);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "On spinner item selected, position= " + position);
        CharSequence selected = (CharSequence) parent.getItemAtPosition(position);
        mLocation.setText(selected);
        if (getResources().getString(R.string.add_location).equals(selected)) {
            Intent intent = new Intent(getContext(), AddLocationActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_LOCATION);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_LOCATION && resultCode == Activity.RESULT_OK) {
            mLocation.setText(data.getStringExtra(AddLocationActivity.EXTRA_NEW_LOCATION));
            updateLocationViewVisible(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
