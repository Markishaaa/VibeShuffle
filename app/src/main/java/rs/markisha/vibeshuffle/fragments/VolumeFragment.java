package rs.markisha.vibeshuffle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.VibeShuffle;
import rs.markisha.vibeshuffle.utils.database.DBHelper;

public class VolumeFragment extends Fragment {

    private DBHelper dbHelper;

    private SeekBar sbChill;
    private SeekBar sbAgro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = VibeShuffle.getDBHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volume, container, false);

        sbChill = view.findViewById(R.id.sbChill);
        sbAgro = view.findViewById(R.id.sbAgro);
        Button btnSave = view.findViewById(R.id.btnVolumeSave);

        sbChill.setProgress(dbHelper.getVolumeOfPlaylistType("chill"));
        sbAgro.setProgress(dbHelper.getVolumeOfPlaylistType("agro"));

        btnSave.setOnClickListener(v -> {
            dbHelper.updateVolumeOfPlaylistType(sbChill.getProgress(), "chill");
            dbHelper.updateVolumeOfPlaylistType(sbAgro.getProgress(), "agro");
        });

        return view;
    }
}