package com.lkk.locationnote.common.data;

import java.util.List;

public interface NotesDataSource {
    void loadNotes(NotesCallback<List<NoteEntity>> callback);

    interface NotesCallback<T> {
        void onSuccess(T result);
        void onFail(String msg);
    }
}
