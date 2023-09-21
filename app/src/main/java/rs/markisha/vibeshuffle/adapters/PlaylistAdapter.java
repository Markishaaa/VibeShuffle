package rs.markisha.vibeshuffle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import rs.markisha.vibeshuffle.R;
import rs.markisha.vibeshuffle.model.Playlist;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    public PlaylistAdapter(Context context, List<Playlist> playlists) {
        super(context, 0, playlists);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Playlist playlist = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = convertView.findViewById(android.R.id.text1);

        // Populate the data into the template view using the data object
        if (playlist != null) {
            tvName.setText(playlist.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        // Create a custom dropdown view by inflating the custom layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_custom_dropdown_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = convertView.findViewById(android.R.id.text1);

        // Populate the data into the template view using the data object
        Playlist playlist = getItem(position);
        if (playlist != null) {
            tvName.setText(playlist.getName());
        }

        return convertView;
    }

}
