package com.lkk.locationnote.common;

import android.content.Context;

import com.lkk.locationnote.common.data.NoteDatabase;
import com.lkk.locationnote.common.data.NotesLocalDataSource;
import com.lkk.locationnote.common.data.NotesRepository;

public class Injection {

    public static NotesRepository provideNotesRepository(Context context) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(context);
        return NotesRepository.getInstance(NotesLocalDataSource.getInstance(noteDatabase.notesDao()));
    }
}
