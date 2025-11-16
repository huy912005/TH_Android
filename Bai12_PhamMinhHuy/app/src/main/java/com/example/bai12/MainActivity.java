package com.example.bai12;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.Normalizer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MusicService.MusicServiceCallback {

    private ArrayList<Song> songList;
    private ListView songView;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    // Mini player Views
    private View miniPlayerLayout;
    private TextView miniTitle, miniArtist;
    private ImageView miniPlayPause, miniNext;

    // Lưu vị trí bài hát hiện tại, đặt mặc định là -1
    private int mCurrentSongPos = -1;


    // ================== SERVICE CONNECTION ================== //
    private final ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setList(songList);
            musicService.setCallback(MainActivity.this);
            musicBound = true;

            if (!songList.isEmpty()) {

                // *** LOGIC MỚI: Xử lý khởi tạo mặc định "Nắng Ấm Trong Tim" (Vị trí 1) ***

                // Điều kiện: Nếu Service đang giữ vị trí 0 VÀ chưa có bài nào được prepare (duration = 0)
                // Đây là dấu hiệu Service khởi động lần đầu tiên hoặc bị reset.
                if (musicService.getSongPos() == 0 && musicService.getDur() == 0) {
                    musicService.loadSongMetadata(1); // Mặc định Nắng Ấm Trong Tim (Vị trí 1)
                }

                // Cập nhật Mini Player dựa trên vị trí mà Service đang giữ
                mCurrentSongPos = musicService.getSongPos();
                updateMiniPlayer(mCurrentSongPos);
                updatePlayPauseButton();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    // ======================================================== //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miniPlayerLayout = findViewById(R.id.mini_player);
        miniTitle = findViewById(R.id.mini_title);
        miniArtist = findViewById(R.id.mini_artist);
        miniPlayPause = findViewById(R.id.mini_play);
        miniNext = findViewById(R.id.mini_next);

        requestAudioPermission();
        songView = findViewById(R.id.song_list);
        loadSongs();

        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);

        // CLICK PLAY SONG & MỞ NOW PLAYING ACTIVITY
        songView.setOnItemClickListener((adapterView, view, pos, id) -> {
            // Cập nhật Service và Play Song
            musicService.setSong(pos);
            musicService.playSong();

            // Lưu lại vị trí bài hát VỪA ĐƯỢC CLICK
            mCurrentSongPos = pos;

            // Mở NowPlaying Activity, truyền vị trí POS qua Intent
            openNowPlayingActivity(pos);
        });

        // ================== MINI PLAYER LISTENERS ================== //
        miniPlayerLayout.setOnClickListener(v -> {
            if (musicService != null && musicBound && musicService.getCurrentSong() != null) {
                // Mở NowPlaying Activity, truyền vị trí hiện tại của Service
                openNowPlayingActivity(musicService.getSongPos());
            }
        });

        miniPlayPause.setOnClickListener(v -> {
            if (musicService != null && musicBound) {
                if (musicService.isPng()) {
                    musicService.pausePlayer();
                } else {
                    // Nếu đang dừng, nhưng chưa được prepare (như khi loadSongMetadata chạy)
                    // thì phải playSong để prepare/start
                    if (musicService.getDur() == 0) {
                        musicService.playSong();
                    } else {
                        musicService.go();
                    }
                }
            }
        });

        miniNext.setOnClickListener(v -> {
            if (musicService != null && musicBound) {
                musicService.playNext();
            }
        });
        // ======================================================== //
    }

    private void openNowPlayingActivity(int pos) {
        Intent intent = new Intent(MainActivity.this, NowPlayingActivity.class);
        intent.putExtra("song_position", pos); // ĐÂY LÀ KHÓA QUAN TRỌNG
        startActivity(intent);
    }

    // ======================== PERMISSION ======================== //
    private void requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 1);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    // ======================== LOAD SONGS (Không đổi) ======================== //
    private void loadSongs() {
        songList = new ArrayList<>();
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor musicCursor = musicResolver.query(
                musicUri,
                projection,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                long id = musicCursor.getLong(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);

                String normalizedTitle = normalizeTitle(title);
                String finalArtist = getArtistFor(title);

                if ("Không rõ".equals(finalArtist) && artist != null) {
                    finalArtist = artist;
                }

                songList.add(new Song(id, normalizedTitle, finalArtist));

            } while (musicCursor.moveToNext());

            musicCursor.close();
        }
    }

    // ================== REMOVE ACCENTS & NORMALIZE (Không đổi) ================== //
    private String removeAccent(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replace(" ", "");
    }

    private String normalizeTitle(String title) {
        String t = removeAccent(title);

        if (t.contains("tuyet")) return "Tuyệt Sắc";
        if (t.contains("lunglinh")) return "Nắng Lung Linh";
        if (t.contains("nangam") || t.contains("lacvao")) return "Nắng Ấm Trong Tim";
        if (t.contains("past")) return "Pastlives";
        if (t.contains("yeuem") || t.contains("quangdang")) return "Anh Đã Không Biết Cách Yêu Em";

        return title;
    }

    private String getArtistFor(String title) {
        String t = removeAccent(title);

        if (t.contains("tuyet")) return "Nam Đức";
        if (t.contains("lunglinh")) return "Nguyễn Thương";
        if (t.contains("nangam") || t.contains("lacvao")) return "DươngG";
        if (t.contains("past")) return "JVKE";
        if (t.contains("yeuem") || t.contains("quangdang")) return "Quang Đăng Trần";

        return "Không rõ";
    }


    // ================= MINI PLAYER UI CONTROL ================= //
    private void updateMiniPlayer(int pos) {
        Song current = songList.get(pos);
        miniTitle.setText(current.getTitle());
        miniArtist.setText(current.getArtist());
    }

    private void updatePlayPauseButton() {
        if (musicService != null && musicBound) {
            if (musicService.isPng()) {
                miniPlayPause.setImageResource(R.drawable.ic_pause);
            } else {
                miniPlayPause.setImageResource(R.drawable.ic_play);
            }
        } else {
            miniPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    // ================== MUSIC SERVICE CALLBACKS ================== //
    @Override
    public void onSongChanged(int newSongPos) {
        if (newSongPos >= 0 && newSongPos < songList.size()) {
            mCurrentSongPos = newSongPos;
            updateMiniPlayer(newSongPos);
        }
        updatePlayPauseButton();
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        updatePlayPauseButton();
    }
    // ============================================================= //

    // ================ SERVICE START/STOP (Không đổi) ===================== //
    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        if (musicBound && musicService != null) {
            musicService.setCallback(this);
            if (musicService.getCurrentSong() != null) {
                mCurrentSongPos = musicService.getSongPos();
                updateMiniPlayer(mCurrentSongPos);
            }
            updatePlayPauseButton();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicService != null) {
            musicService.setCallback(null);
        }
    }

    // ================= MENU (Không đổi) ===================== //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}