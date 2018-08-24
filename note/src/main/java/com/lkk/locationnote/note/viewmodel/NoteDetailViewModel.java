package com.lkk.locationnote.note.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

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

public class NoteDetailViewModel extends AndroidViewModel {

    private static final String TAG = NoteDetailViewModel.class.getSimpleName();

    private Context mContext;
    private NotesRepository mRepository;
    private MutableLiveData<NoteEntity> mNote = new MutableLiveData<>();
    private MutableLiveData<String> mAddress = new MutableLiveData<>();

    public NoteDetailViewModel(@NonNull Application application) {
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

    public MutableLiveData<String> getAddress() {
        return mAddress;
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
                mAddress.setValue(address);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {}
        });
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }
}
