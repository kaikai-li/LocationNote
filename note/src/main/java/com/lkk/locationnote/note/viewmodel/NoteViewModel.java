package com.lkk.locationnote.note.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lkk.locationnote.common.Injection;
import com.lkk.locationnote.common.data.NoteDatabase;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.data.NotesDataSource;
import com.lkk.locationnote.common.data.NotesLocalDataSource;
import com.lkk.locationnote.common.data.NotesRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NotesRepository mRepository;
    private MutableLiveData<List<NoteEntity>> mNotes = new MutableLiveData<>();

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = Injection.provideNotesRepository(application.getApplicationContext());
    }

    public void loadNotes() {
        mRepository.loadNotes(new NotesDataSource.NotesCallback<List<NoteEntity>>() {
            @Override
            public void onSuccess(List<NoteEntity> result) {

            }

            @Override
            public void onFail() {

            }
        });
    }
}
