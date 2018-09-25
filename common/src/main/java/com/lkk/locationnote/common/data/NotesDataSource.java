package com.lkk.locationnote.common.data;

import android.support.annotation.IntRange;

import java.util.List;

public interface NotesDataSource {
    void loadNotes(NotesCallback<List<NoteEntity>> callback);
    void getNote(@IntRange(from = 0) int noteId, NotesCallback<NoteEntity> callback);
    void insertNote(NoteEntity note, NotesCallback<Boolean> callback);
    void updateNote(NoteEntity note, NotesCallback<Integer> callback);
    void deleteNoteById(int id, NotesCallback<Integer> callback);

    interface NotesCallback<T> {
        void onSuccess(T result);
        void onFail(String msg);
    }
}
