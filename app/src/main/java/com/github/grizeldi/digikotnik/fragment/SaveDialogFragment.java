package com.github.grizeldi.digikotnik.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.github.grizeldi.digikotnik.R;
import com.github.grizeldi.digikotnik.data.DatabaseHolder;
import com.github.grizeldi.digikotnik.data.Measurement;
import com.github.grizeldi.digikotnik.data.MeasurementDao;

import java.util.Arrays;

public class SaveDialogFragment extends DialogFragment {
    private final int[] angles;
    private EditText nameTextBox;

    public SaveDialogFragment(int... values) {
        super();
        if (values.length != 3)
            throw new IllegalArgumentException();
        angles = values;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_save, null);

        nameTextBox = root.findViewById(R.id.saveDialog_textbox);
        ((TextView)root.findViewById(R.id.saveDialog_azimuth)).setText(getString(R.string.azimuth, angles[0]));
        ((TextView)root.findViewById(R.id.saveDialog_pitch)).setText(getString(R.string.pitch, angles[1]));
        ((TextView)root.findViewById(R.id.saveDialog_roll)).setText(getString(R.string.roll, angles[2]));

        builder.setTitle(getString(R.string.save))
            .setView(root)
            .setPositiveButton(R.string.confirm, (dialogInterface, id) -> {
                Toast.makeText(getActivity(), "Measurement saved.", Toast.LENGTH_SHORT).show();

                //Store data
                Measurement measurement = new Measurement();
                measurement.name = nameTextBox.getText().toString();
                measurement.azimuth = angles[0];
                measurement.pitch = angles[1];
                measurement.roll = angles[2];

                new Thread(() -> {
                    MeasurementDao dao = DatabaseHolder.database.measurementDao();
                    dao.addMeasurement(measurement);
                }).start();
            })
            .setNegativeButton(R.string.cancel, ((dialogInterface, id) -> {
                getDialog().cancel();
            }));
        return builder.create();
    }
}
