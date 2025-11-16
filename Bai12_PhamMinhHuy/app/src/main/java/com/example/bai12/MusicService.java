package com.example.bai12;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import java.util.ArrayList;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    // Interface Callback để thông báo trạng thái cho Activity
    public interface MusicServiceCallback {
        void onSongChanged(int newSongPos);
        void onPlaybackStateChanged(boolean isPlaying);
    }

    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPos = 0;
    private final IBinder musicBind = new MusicBinder();
    private MusicServiceCallback callback;

    public void setCallback(MusicServiceCallback callback) {
        this.callback = callback;
    }

    public void onCreate() {
        super.onCreate();
        songPos = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                android.os.PowerManager.PARTIAL_WAKE_LOCK);

        player.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    public void setSong(int songIndex) {
        songPos = songIndex;
    }

    public int getSongPos() {
        return songPos;
    }

    public Song getCurrentSong() {
        if (songs != null && !songs.isEmpty() && songPos >= 0 && songPos < songs.size()) {
            return songs.get(songPos);
        }
        return null;
    }

    // Phương thức load metadata mà không prepare/start
    public void loadSongMetadata(int songIndex) {
        songPos = songIndex;
        player.reset();

        Song playSong = songs.get(songPos);
        long currSong = playSong.getID();

        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong
        );

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (callback != null) {
            callback.onSongChanged(songPos);
        }
    }

    public void playSong() {
        player.reset();

        Song playSong = songs.get(songPos);
        long currSong = playSong.getID();

        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong
        );

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.prepareAsync();

        if (callback != null) {
            callback.onSongChanged(songPos);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (callback != null) {
            callback.onPlaybackStateChanged(true);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public void playNext() {
        songPos++;
        if (songPos >= songs.size()) songPos = 0;
        playSong();
    }

    public void playPrev() {
        songPos--;
        if (songPos < 0) songPos = songs.size() - 1;
        playSong();
    }

    public boolean isPng() {
        try {
            return player.isPlaying();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public int getDur() {
        try {
            return player.getDuration();
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public int getPosn() {
        try {
            return player.getCurrentPosition();
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public void go() {
        player.start();
        if (callback != null) {
            callback.onPlaybackStateChanged(true);
        }
    }

    public void pausePlayer() {
        player.pause();
        if (callback != null) {
            callback.onPlaybackStateChanged(false);
        }
    }

    public void seek(int pos) {
        player.seekTo(pos);
    }
}