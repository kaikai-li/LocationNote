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

    @Override
    public void getNote(final int noteId, final NotesCallback<NoteEntity> callback) {
        Single.fromCallable(new Callable<NoteEntity>() {

            @Override
            public NoteEntity call() throws Exception {
                return mNotesDao.getNoteById(noteId);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableSingleObserver<NoteEntity>() {
            @Override
            public void onSuccess(NoteEntity noteEntity) {
                Log.d(TAG, "Get note success! result= " + noteEntity);
                if (callback != null) {
                    callback.onSuccess(noteEntity);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Get note fail, " + e.getMessage());
                if (callback != null) {
                    callback.onFail(e.getMessage());
                }
            }
        });
    }

    @Override
    public void insertNote(final NoteEntity note, final NotesCallback<Boolean> callback) {
        Single.fromCallable(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                mNotesDao.insertNotes(note);
                return true;
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableSingleObserver<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Log.d(TAG, "Insert note success!");
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Insert note error, " + e.getMessage());
                if (callback != null) {
                    callback.onFail(e.getMessage());
                }
            }
        });
    }

    @Override
    public void updateNote(final NoteEntity note, final NotesCallback<Integer> callback) {
        Single.fromCallable(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return mNotesDao.updateNotes(note);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableSingleObserver<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "Update note success, result= " + integer);
                if (callback != null) {
                    callback.onSuccess(integer);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Update note error, " + e.getMessage());
                if (callback != null) {
                    callback.onFail(e.getMessage());
                }
            }
        });
    }
}
