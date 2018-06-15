package com.lkk.locationnote.common.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static final Object mLock = new Object();

    private static NoteDatabase mInstance;

    public abstract NotesDao notesDao();

    public static NoteDatabase getInstance(Context context) {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        NoteDatabase.class, "LocationNote.db")
                        .build();
            }
            return mInstance;
        }
    }
}
