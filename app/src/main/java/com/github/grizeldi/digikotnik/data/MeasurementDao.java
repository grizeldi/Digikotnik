package com.github.grizeldi.digikotnik.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeasurementDao {
    @Query("SELECT * FROM measurement")
    List<Measurement> getMeasurements();
    @Query("SELECT * FROM measurement ORDER BY uid DESC LIMIT 10")
    List<Measurement> getLastTenMeasurements();
    @Insert
    void addMeasurement(Measurement measurement);
    @Delete
    void deleteMeasurement(Measurement measurement);
}
