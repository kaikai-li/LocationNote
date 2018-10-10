package com.lkk.locationnote.note.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lkk.locationnote.common.Injection;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.data.NotesDataSource;
import com.lkk.locationnote.common.data.NotesRepository;
import com.lkk.locationnote.common.log.Log;

import java.util.ArrayList;
import java.util.List;

public class AddEditViewModel extends AndroidViewModel {

    private static final String TAG = AddEditViewModel.class.getSimpleName();

    private Context mContext;
    private NotesRepository mRepository;
    private MutableLiveData<NoteEntity> mNote = new MutableLiveData<>();
    private MutableLiveData<Void> mNoteUpdated = new MutableLiveData<>();
    private MutableLiveData<NoteLocation> mLocation = new MutableLiveData<>();

    public AddEditViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepository = Injection.provideNotesRepository(mContext);
    }

    public MutableLiveData<NoteEntity> getNote() {
        return mNote;
    }

    public void getNote(int noteId) {
        Log.d(TAG, "Get note, id= " + noteId);
        if (noteId < 0) {
            return;
        }
        mRepository.getNote(noteId, new NotesDataSource.NotesCallback<NoteEntity>() {
            @Override
            public void onSuccess(NoteEntity result) {
                mNote.setValue(result);
            }

            @Override
            public void onFail(String msg) {}
        });
    }

    public MutableLiveData<Void> getNoteUpdated() {
        return mNoteUpdated;
    }

    public void insertNote(NoteEntity noteEntity) {
        if (noteEntity == null) {
            return;
        }

        mRepository.insertNote(noteEntity, new NotesDataSource.NotesCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mNoteUpdated.setValue(null);
            }

            @Override
            public void onFail(String msg) {}
        });
    }

    public void updateNote(NoteEntity noteEntity) {
        if (noteEntity == null) {
            return;
        }

        mRepository.updateNote(noteEntity, new NotesDataSource.NotesCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                mNoteUpdated.setValue(null);
            }

            @Override
            public void onFail(String msg) {}
        });
    }

    public MutableLiveData<NoteLocation> getLocation() {
        return mLocation;
    }

    public void startLocation() {
        final AMapLocationClient locationClient = new AMapLocationClient(mContext);
        AMapLocationListener locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.d(TAG, "Start location result, aMapLocation= " + aMapLocation);
                NoteLocation location = null;
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    List<String> addresses = new ArrayList<>();
                    if (!TextUtils.isEmpty(aMapLocation.getAoiName())) {
                        addresses.add(aMapLocation.getAoiName());
                    }
                    if (!TextUtils.isEmpty(aMapLocation.getPoiName())) {
                        addresses.add(aMapLocation.getPoiName());
                    }
                    if (!TextUtils.isEmpty(aMapLocation.getStreet())) {
                        addresses.add(aMapLocation.getStreet());
                    }
                    if (!TextUtils.isEmpty(aMapLocation.getDescription())) {
                        addresses.add(aMapLocation.getDescription());
                    }
                    location = new NoteLocation(addresses, aMapLocation.getLongitude(), aMapLocation.getLatitude());
                }
                mLocation.setValue(location);
                locationClient.stopLocation();
                locationClient.onDestroy();
            }
        };
        locationClient.setLocationListener(locationListener);
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setOnceLocation(true);
        locationOption.setOnceLocationLatest(true);
        locationOption.setNeedAddress(true);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }

    public static class NoteLocation {
        private final List<String> mAddresses;
        private final double mLongitude;
        private final double mLatitude;

        public NoteLocation(List<String> addresses, double longitude, double latitude) {
            mAddresses = addresses;
            mLongitude = longitude;
            mLatitude = latitude;
        }

        public List<String> getAddresses() {
            return mAddresses;
        }

        public double getLongitude() {
            return mLongitude;
        }

        public double getLatitude() {
            return mLatitude;
        }
    }
}
