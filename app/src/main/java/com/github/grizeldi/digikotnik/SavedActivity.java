package com.github.grizeldi.digikotnik;

import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.grizeldi.digikotnik.data.DatabaseHolder;
import com.github.grizeldi.digikotnik.data.Measurement;

import java.util.List;

public class SavedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        ListView listView = findViewById(R.id.savedList);
        new Thread(() -> {
            List<Measurement> entries = DatabaseHolder.database.measurementDao().getLastTenMeasurements();
            listView.setAdapter(new MeasurementAdapter(SavedActivity.this, entries));
        }).start();
    }
}