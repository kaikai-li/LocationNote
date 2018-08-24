package com.lkk.locationnote.note.event;

public class NoteEditEvent {
    private int noteId;

    public NoteEditEvent(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }
}
