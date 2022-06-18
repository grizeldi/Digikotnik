package com.github.grizeldi.digikotnik.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeasurementDao {
    @Query("SELECT * FROM measurement")
    List<Measurement> getMeasurements();
    @Insert
    void addMeasurement(Measurement measurement);
}
