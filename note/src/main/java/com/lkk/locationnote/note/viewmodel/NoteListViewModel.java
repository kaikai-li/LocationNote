package com.lkk.locationnote.note.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lkk.locationnote.common.Injection;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.data.NotesDataSource;
import com.lkk.locationnote.common.data.NotesRepository;
import com.lkk.locationnote.common.log.Log;

import java.util.List;

public class NoteListViewModel extends AndroidViewModel {

    private static final String TAG = NoteListViewModel.class.getSimpleName();

    private NotesRepository mRepository;
    private MutableLiveData<List<NoteEntity>> mNotes = new MutableLiveData<>();
    private MutableLiveData<Integer> mOpenNoteEvent = new MutableLiveData<>();

    public NoteListViewModel(@NonNull Application application) {
        super(application);
        mRepository = Injection.provideNotesRepository(application.getApplicationContext());
    }

    public MutableLiveData<List<NoteEntity>> getNotes() {
        return mNotes;
    }

    public void loadNotes() {
        mRepository.loadNotes(new NotesDataSource.NotesCallback<List<NoteEntity>>() {
            @Override
            public void onSuccess(List<NoteEntity> result) {
                Log.d(TAG, "Load notes success, result= " + result);
                mNotes.setValue(result);
            }

            @Override
            public void onFail(String msg) {
                Log.e(TAG, "Load notes fail, " + msg);
            }
        });
    }

    public MutableLiveData<Integer> getOpenNoteEvent() {
        return mOpenNoteEvent;
    }
}
