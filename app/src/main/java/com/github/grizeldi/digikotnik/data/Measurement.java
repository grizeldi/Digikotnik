package com.github.grizeldi.digikotnik.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Measurement {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "azimuth")
    public int azimuth;
    @ColumnInfo(name = "pitch")
    public int pitch;
    @ColumnInfo(name = "roll")
    public int roll;

    @Override
    public String toString() {
        return "Measurement{" +
                "name='" + name + '\'' +
                ", azimuth=" + azimuth +
                ", pitch=" + pitch +
                ", roll=" + roll +
                '}';
    }
}
