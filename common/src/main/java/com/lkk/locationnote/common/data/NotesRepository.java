package com.lkk.locationnote.common.data;

import java.util.List;

public class NotesRepository implements NotesDataSource {
    private static volatile NotesRepository mInstance;

    private NotesDataSource mLocalDataSource;

    private NotesRepository(NotesDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    public static NotesRepository getInstance(NotesDataSource localDataSource) {
        if (mInstance == null) {
            synchronized (NotesRepository.class) {
                if (mInstance == null) {
                    mInstance = new NotesRepository(localDataSource);
                }
            }
        }

        return mInstance;
    }

    @Override
    public void loadNotes(NotesCallback<List<NoteEntity>> callback) {
        mLocalDataSource.loadNotes(callback);
    }
}
