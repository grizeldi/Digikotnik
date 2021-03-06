package com.github.grizeldi.digikotnik.fragment;

import android.os.Bundle;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.grizeldi.digikotnik.MeasurementAdapter;
import com.github.grizeldi.digikotnik.R;
import com.github.grizeldi.digikotnik.data.DatabaseHolder;
import com.github.grizeldi.digikotnik.data.Measurement;

import java.util.List;

public class RecentFragment extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recent, container, false);
        listView = root.findViewById(R.id.recentList);
        updateData();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData(){
        if (listView == null)
            return;
        new Thread(() -> {
            List<Measurement> entries = DatabaseHolder.database.measurementDao().getLastTenMeasurements();
            listView.post(() -> listView.setAdapter(new MeasurementAdapter(getContext(), entries)));
        }).start();
    }
}