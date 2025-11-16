package com.example.bai12;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class NowPlayingActivity extends AppCompatActivity implements MusicService.MusicServiceCallback {

    private MusicService musicService;
    private boolean musicBound = false;
    private int clickedSongPosition = -1; // Vị trí bài hát được truyền từ MainActivity

    // Views
    private ImageView btnBack;
    private TextView tvTitle, tvArtist;
    private ImageView ivAlbumArt;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalDuration;
    private ImageView btnPrev, btnPlayPause, btnNext;

    // Animation
    private ObjectAnimator rotator;
    private final Handler handler = new Handler();
    private final Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (musicService != null && musicService.isPng()) {
                int currentPos = musicService.getPosn();
                seekBar.setProgress(currentPos);
                tvCurrentTime.setText(formatTime(currentPos));
                handler.postDelayed(this, 1000);
            }
        }
    };

    // ================== SERVICE CONNECTION ================== //
    private final ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setCallback(NowPlayingActivity.this);
            musicBound = true;

            // XỬ LÝ QUAN TRỌNG: Kiểm tra và phát đúng bài hát chỉ khi cần
            if (clickedSongPosition != -1) {
                // Kiểm tra: Nếu Service đang giữ VỊ TRÍ KHÁC HOẶC Service chưa phát,
                // thì setSong và playSong.
                if (musicService.getSongPos() != clickedSongPosition || !musicService.isPng()) {
                    musicService.setSong(clickedSongPosition);
                    musicService.playSong();
                }
            }

            setupPlayer();
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
        setContentView(R.layout.activity_now_playing);

        // ĐỌC VỊ TRÍ BÀI HÁT TỪ INTENT
        if (getIntent().hasExtra("song_position")) {
            clickedSongPosition = getIntent().getIntExtra("song_position", -1);
        }

        initViews();
        initRotator();
        setListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.now_playing_title);
        tvArtist = findViewById(R.id.now_playing_artist);
        ivAlbumArt = findViewById(R.id.album_art);
        seekBar = findViewById(R.id.seek_bar);
        tvCurrentTime = findViewById(R.id.text_current_time);
        tvTotalDuration = findViewById(R.id.text_total_duration);
        btnPrev = findViewById(R.id.btn_prev);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        btnNext = findViewById(R.id.btn_next);

        btnPlayPause.setImageResource(R.drawable.ic_play_black);
    }

    private void initRotator() {
        rotator = ObjectAnimator.ofFloat(ivAlbumArt, "rotation", 0f, 360f);
        rotator.setDuration(25000);
        rotator.setInterpolator(new LinearInterpolator());
        rotator.setRepeatCount(ObjectAnimator.INFINITE);
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnPlayPause.setOnClickListener(v -> {
            if (musicService == null) return;
            if (musicService.isPng()) {
                musicService.pausePlayer();
            } else {
                // Nếu đang dừng, và chưa có bài nào được prepare, ta cần play bài hiện tại.
                // Tuy nhiên, logic này sẽ được xử lý qua callback khi click
                musicService.go();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (musicService == null) return;
            musicService.playNext();
        });

        btnPrev.setOnClickListener(v -> {
            if (musicService == null) return;
            musicService.playPrev();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seek(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* Không cần làm gì */ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* Không cần làm gì */ }
        });
    }

    private void setupPlayer() {
        if (musicService == null) return;

        Song currentSong = musicService.getCurrentSong();
        if (currentSong != null) {
            tvTitle.setText(currentSong.getTitle());
            tvArtist.setText(currentSong.getArtist());
        }

        // Cập nhật Seek Bar chỉ khi Media Player đã sẵn sàng
        // Dùng try-catch để tránh crash nếu getDur() bị IllegalStateException
        try {
            seekBar.setMax(musicService.getDur());
            tvTotalDuration.setText(formatTime(musicService.getDur()));
            // Bắt đầu cập nhật Seek Bar
            handler.post(updateSeekBar);
        } catch (IllegalStateException e) {
            // Nếu chưa sẵn sàng, giữ giá trị mặc định và chờ onSongChanged
            seekBar.setMax(0);
            tvTotalDuration.setText("00:00");
        }


        updatePlayPauseUI();
    }

    private void updatePlayPauseUI() {
        if (musicService.isPng()) {
            btnPlayPause.setImageResource(R.drawable.ic_pause_black);
            if (!rotator.isStarted()) rotator.start();
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_play_black);
            rotator.pause();
        }
    }

    private String formatTime(int ms) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                (ms / 1000) / 60,
                (ms / 1000) % 60);
    }

    // ================== MUSIC SERVICE CALLBACKS ================== //
    @Override
    public void onSongChanged(int newSongPos) {
        setupPlayer();
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        updatePlayPauseUI();
        // Cập nhật lại Seek Bar khi phát/dừng
        if (isPlaying) {
            handler.post(updateSeekBar);
        } else {
            handler.removeCallbacks(updateSeekBar);
        }
    }
    // ============================================================= //


    @Override
    protected void onStart() {
        super.onStart();
        Intent playIntent = new Intent(this, MusicService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicBound) {
            // Chỉ unbind service nếu activity này bị stop
            unbindService(musicConnection);
            musicBound = false;
        }
        rotator.end();
        handler.removeCallbacks(updateSeekBar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicService != null) {
            musicService.setCallback(null);
        }
    }
}