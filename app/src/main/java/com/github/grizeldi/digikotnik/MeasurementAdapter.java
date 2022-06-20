package com.github.grizeldi.digikotnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.grizeldi.digikotnik.data.DatabaseHolder;
import com.github.grizeldi.digikotnik.data.Measurement;

import java.util.List;

public class MeasurementAdapter extends ArrayAdapter<Measurement> {
    public List<Measurement> model;
    public MeasurementAdapter(@NonNull Context context, @NonNull List<Measurement> objects) {
        super(context, 0, objects);
        model = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_measurement, parent, false);
        }
        Measurement data = getItem(position);
        ((TextView)convertView.findViewById(R.id.entryName)).setText(data.name);
        ((TextView)convertView.findViewById(R.id.measurement_azimuth)).setText(getContext().getString(R.string.azimuth, data.azimuth));
        ((TextView)convertView.findViewById(R.id.measurement_pitch)).setText(getContext().getString(R.string.pitch, data.pitch));
        ((TextView)convertView.findViewById(R.id.measurement_roll)).setText(getContext().getString(R.string.roll, data.roll));
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(view -> {
            int deletedPosition = (Integer) view.getTag();
            Measurement toBeDeleted = getItem(deletedPosition);
            new Thread(() -> { // I too love async task ping pong
                DatabaseHolder.database.measurementDao().deleteMeasurement(toBeDeleted);
                view.post(() -> {
                    model.remove(deletedPosition);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Measurement deleted.", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

        return convertView;
    }
}
