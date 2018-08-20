package com.lkk.locationnote.common.data;

import com.lkk.locationnote.common.log.Log;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NotesLocalDataSource implements NotesDataSource {

    private static final String TAG = NotesLocalDataSource.class.getSimpleName();

    private static volatile NotesLocalDataSource mInstance;

    private NotesDao mNotesDao;

    private NotesLocalDataSource(NotesDao notesDao) {
        mNotesDao = notesDao;
    }

    public static NotesLocalDataSource getInstance(NotesDao notesDao) {
        if (mInstance == null) {
            synchronized (NotesLocalDataSource.class) {
                if (mInstance == null) {
                    mInstance = new NotesLocalDataSource(notesDao);
                }
            }
        }

        return mInstance;
    }

    @Override
    public void loadNotes(final NotesCallback<List<NoteEntity>> callback) {
        Single.fromCallable(new Callable<List<NoteEntity>>() {

            @Override
            public List<NoteEntity> call() throws Exception {
                return mNotesDao.loadAllNotes();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableSingleObserver<List<NoteEntity>>() {

            @Override
            public void onSuccess(List<NoteEntity> noteEntities) {
                Log.d(TAG, "Load notes success!");
                if (callback != null) {
                    callback.onSuccess(noteEntities);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Load notes error, e= " + e.getMessage());
                if (callback != null) {
                    callback.onFail(e.getMessage());
                }
            }
        });
    }
}
