package android.helloworld.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class JukeboxActivity extends AppCompatActivity {
    private String songUrl = "http://mad.mywork.gr/get_song.php?t=1546";
    static TextView txtArtist;
    static TextView txtTitle;
    static TextView txtUrl;
    static TextView tv_status;
    ImageButton btn_play, btn_pause, btn_request;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jukebox);

        txtArtist = findViewById(R.id.txtArtist);
        txtTitle = findViewById(R.id.txtTitle);
        txtUrl = findViewById(R.id.txtUrl);
        tv_status = findViewById(R.id.tv_status);
        btn_play = findViewById(R.id.btn_play);
        btn_pause= findViewById(R.id.btn_pause);
        btn_request = findViewById(R.id.btn_request);

        btn_play.setEnabled(false);
        btn_pause.setEnabled(false);

        btn_request.setOnClickListener(
                v -> {
                    RemoteContent RCT = new RemoteContent();
                    String url = songUrl;
                    RCT.execute(url);
                    tv_status.setText("Requesting a song from CTower");
                    btn_play.setEnabled(true);
                    btn_pause.setEnabled(false);
            }
        );

        btn_play.setOnClickListener(
                v -> {
                    if(!isPlaying){
                        playAudio();
                        tv_status.setText("Playing");
                        btn_play.setEnabled(false);
                        btn_pause.setEnabled(true);
                    }
                }
        );

        btn_pause.setOnClickListener(
                v -> {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        btn_play.setEnabled(true);
                        btn_pause.setEnabled(false);
                        tv_status.setText("Stopped");
                        Toast.makeText(JukeboxActivity.this, "Audio has been paused", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(JukeboxActivity.this, "Audio has not played", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void playAudio() {
        String audioUrl = txtUrl.getText().toString();

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            if (isFinishing()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

 }
