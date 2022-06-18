package com.github.grizeldi.digikotnik.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.grizeldi.digikotnik.R;

public class MeasurementFragment extends Fragment implements SensorEventListener {
    public static final boolean USE_FALLBACK_SENSORS = false;
    private SensorManager sensorManager;
    private Sensor accelometerSensor, magneticSensor, dirVectorSensor;

    // Sensor data cache
    private float[] accelometerCache = new float[3];
    private float[] magnetCache = new float[3];
    private float[] directionCache = new float[5];

    private float[] outputCache, originOrientation; // Everything in here is stored in radians (values from -pi to pi)

    // Views
    private TextView angleDisplayText;

    public MeasurementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        accelometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        dirVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        originOrientation = new float[3];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_measurement, container, false);
        angleDisplayText = root.findViewById(R.id.angleDisplayText);
        root.findViewById(R.id.resetButton).setOnClickListener(view -> {
            // Copy the values over to the origin. Can't just reassign the array because references
            originOrientation[0] = outputCache[0];
            originOrientation[1] = outputCache[1];
            originOrientation[2] = outputCache[2];
        });
        root.findViewById(R.id.saveButton).setOnClickListener(view -> {
            SaveDialogFragment dialog = new SaveDialogFragment(radToDeg(outputCache[0] - originOrientation[0]), radToDeg(outputCache[1] - originOrientation[1]), radToDeg(outputCache[2] - originOrientation[2]));
            dialog.show(getFragmentManager(), "savedialog");
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (USE_FALLBACK_SENSORS) {
            sensorManager.registerListener(this, accelometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            sensorManager.registerListener(this, dirVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == accelometerSensor) {
            accelometerCache = sensorEvent.values;
        } else if (sensorEvent.sensor == magneticSensor) {
            magnetCache = sensorEvent.values;
        } else if (sensorEvent.sensor == dirVectorSensor) {
            directionCache = sensorEvent.values;
        }
        updateValues();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    private void updateValues() {
        final float[] rotationMatrix = new float[9];
        final float[] orientationAngles = new float[3];
        if (USE_FALLBACK_SENSORS)
            SensorManager.getRotationMatrix(rotationMatrix, null, accelometerCache, magnetCache);
        else
            SensorManager.getRotationMatrixFromVector(rotationMatrix, directionCache);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        outputCache = orientationAngles;
        int angle = radToDeg(orientationAngles[0] - originOrientation[0]);
        angleDisplayText.setText(getString(R.string.angle_display_text, angle));
    }

    private int radToDeg(float radians){
        return (int) (radians / Math.PI * 180);
    }
}