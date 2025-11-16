package com.example.bai12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Song> songList;

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songList = songs;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int i) {
        return songList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return songList.get(i).getID();
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.song_row, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.song_icon);
        TextView title = convertView.findViewById(R.id.song_title);
        TextView artist = convertView.findViewById(R.id.song_artist);

        Song s = songList.get(pos);

        title.setText(s.getTitle());
        artist.setText(s.getArtist().equals("<unknown>") ? "Unknown" : s.getArtist());

        return convertView;
    }
}
