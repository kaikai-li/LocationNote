package com.lkk.locationnote.common.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "notes")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private long time;
    private double longitude;
    private double latitude;

    public NoteEntity(){}

    private NoteEntity(Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setContent(builder.content);
        setTime(builder.time);
        setLongitude(builder.longitude);
        setLatitude(builder.latitude);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "NoteEntity:id= " + id + ",title= " + title + ",content= " + content + ",time= "
                + time + ",longitude= " + longitude + ",latitude= " + latitude;
    }

    public static final class Builder {
        private int id;
        private String title;
        private String content;
        private long time;
        private double longitude;
        private double latitude;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder time(long val) {
            time = val;
            return this;
        }

        public Builder longitude(double val) {
            longitude = val;
            return this;
        }

        public Builder latitude(double val) {
            latitude = val;
            return this;
        }

        public NoteEntity build() {
            return new NoteEntity(this);
        }
    }
}
