package com.github.grizeldi.digikotnik.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.github.grizeldi.digikotnik.R;

import java.util.Arrays;

public class SaveDialogFragment extends DialogFragment {
    private int[] angles;
    public SaveDialogFragment(int... values) {
        super();
        if (values.length != 3)
            throw new IllegalArgumentException();
        angles = values;
        System.out.println("Angles currently: " + Arrays.toString(angles));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_save, null);
        ((TextView)root.findViewById(R.id.saveDialog_azimuth)).setText(getString(R.string.azimuth, angles[0]));
        ((TextView)root.findViewById(R.id.saveDialog_pitch)).setText(getString(R.string.pitch, angles[1]));
        ((TextView)root.findViewById(R.id.saveDialog_roll)).setText(getString(R.string.roll, angles[2]));

        builder.setTitle(getString(R.string.save))
            .setView(root)
            .setPositiveButton(R.string.confirm, (dialogInterface, id) -> {
                Toast.makeText(getActivity(), "congrats, you found the ok button", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton(R.string.cancel, ((dialogInterface, id) -> {
                getDialog().cancel();
            }));
        return builder.create();
    }
}
