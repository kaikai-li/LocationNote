package com.lkk.locationnote.note.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lkk.locationnote.common.Injection;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.data.NotesDataSource;
import com.lkk.locationnote.common.data.NotesRepository;
import com.lkk.locationnote.common.log.Log;

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
        mRepository = Injection.provideNotesRepository(application.getApplicationContext());
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
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    NoteLocation location = new NoteLocation(aMapLocation.getAddress(),
                            aMapLocation.getLongitude(), aMapLocation.getLatitude());
                    mLocation.setValue(location);
                } else {
                    mLocation.setValue(null);
                }
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

    public void startRegeocode(final LatLonPoint point) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(mContext);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
                Log.d(TAG, "onRegeocodeSearched, code= " + code);
                // 1000为成功，其他为失败
                String address = null;
                if (code == 1000) {
                    address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                }

                NoteLocation location = new NoteLocation(address,
                        point.getLongitude(), point.getLatitude());
                mLocation.setValue(location);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {}
        });
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    public static class NoteLocation {
        private final String mAddress;
        private final double mLongitude;
        private final double mLatitude;

        public NoteLocation(String address, double longitude, double latitude) {
            mAddress = address;
            mLongitude = longitude;
            mLatitude = latitude;
        }

        public String getAddress() {
            return mAddress;
        }

        public double getLongitude() {
            return mLongitude;
        }

        public double getLatitude() {
            return mLatitude;
        }
    }
}
