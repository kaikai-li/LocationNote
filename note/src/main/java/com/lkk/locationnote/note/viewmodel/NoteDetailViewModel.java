package com.lkk.locationnote.note.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

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
}
