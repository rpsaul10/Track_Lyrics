package com.example.lyrics_test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMain extends Activity {
    EditText trackName, artistName;
    Button btnSearch;
    TextView lyrics;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        trackName = findViewById(R.id.t_name);
        artistName = findViewById(R.id.art_name);
        btnSearch = findViewById(R.id.btn_search);
        lyrics = findViewById(R.id.txt_lyrics);

        btnSearch.setOnClickListener(view -> {
            btnPress();
        });
    }

    private void btnPress() {
        if (trackName.getText().toString().equals(" ") || artistName.getText().toString().equals(" ")) {
            lyrics.setText("");
            Toast.makeText(this, "Be sure to fill in the fields", Toast.LENGTH_LONG).show();
            return;
        }
        LyricSong lyricSong = new LyricSong(trackName.getText().toString(), artistName.getText().toString());
        new DownloadLyrics(this, lyricSong).start();
    }
}



class DownloadLyrics extends Thread {
    ActivityMain context;
    LyricSong lyricSong;

    public DownloadLyrics(ActivityMain context, LyricSong lyricSong) {
        this.context = context;
        this.lyricSong = lyricSong;
    }

    @Override
    public void run() {
        String ly_temp = lyricSong.getTrackLyric();
        context.runOnUiThread(()-> {
            if (ly_temp == null) {
                context.lyrics.setText("");
                Toast.makeText(context, "Lyrics not available, try with another song", Toast.LENGTH_LONG).show();
                return;
            }
            if (ly_temp.equals("")) {
                context.lyrics.setText("");
                Toast.makeText(context, "Forbidden lyrics, try with another song", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(context, "Lyrics downloaded successfully", Toast.LENGTH_LONG).show();
            context.lyrics.setText(ly_temp);
        });
    }
}