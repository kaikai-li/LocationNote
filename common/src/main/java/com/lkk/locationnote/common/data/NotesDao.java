package com.lkk.locationnote.common.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNotes(NoteEntity... notes);
    @Delete
    public int deleteNotes(NoteEntity... notes);
    @Update
    public int updateNotes(NoteEntity... notes);
    @Query("SELECT * FROM notes")
    public List<NoteEntity> loadAllNotes();
}
