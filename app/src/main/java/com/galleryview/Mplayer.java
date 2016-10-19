package com.galleryview;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by USER on 2016-10-19.
 */
public class Mplayer extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {
    ArrayList<String> musicListStr = new ArrayList<String>();
    String musicDir = Environment.getExternalStorageDirectory().toString() + "/Music";
    // String musicDir = "/sdcard/Music";
    MediaPlayer mplayer = null;
    boolean isPlaying = false;
    boolean isChanged = true;
    String selectFilePath = "";
    int selectPosition = 0;
    MediaPlayer mp;
    ProgressBar progressBar;
    SeekBar seekBar;
    TextView tvTime;
    ListView listView;
    boolean isShuffle, isRepeat;
    int seekValue = 0;

    Handler h = new Handler();

    Runnable r = new Runnable() {
        @Override
        public void run() {
            int progress = mp.getCurrentPosition();
            seekBar.setProgress(progress);
            tvTime.setText(dataFormat.format(progress));
            if (isPlaying) h.postDelayed(this, 900);

            Log.d("d", "progress : " + progress);
            Log.d("d", "mp.isPlaying() : " + mp.isPlaying());
            Log.d("d", "isPlaying : " + isPlaying);
        }
    };

    public void timeThread() {
        h.removeCallbacks(r);
        h.post(r);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mplayer);
        File[] files = new File(musicDir).listFiles();
        for (File file : files) musicListStr.add(file.getName());
        //
        ArrayAdapter<String> arad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, musicListStr);
        listView = (ListView) findViewById(R.id.lvMusicList);
        listView.setAdapter(arad);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(selectPosition, true);
        //
        selectFilePath = musicDir + "/" + musicListStr.get(selectPosition);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                selectFilePath = musicDir + "/" + musicListStr.get(position);
                isChanged = true;
            }
        });
        //
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTime.setText(dataFormat.format(progress));
                mp.seekTo(progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        tvTime = (TextView) findViewById(R.id.tvTime);
        //
        btnBackward = (Button) findViewById(R.id.btnBackward);
        btnPlayPause = (Button) findViewById(R.id.btnPlayPause);
        btnForward = (Button) findViewById(R.id.btnForward);

        btnRepeat = (Button) findViewById(R.id.btnRepeat);
        btnShuffle = (Button) findViewById(R.id.btnShuffle);
        btnNone = (Button) findViewById(R.id.btnNone);

        btnBackward.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnForward.setOnClickListener(this);

        btnRepeat.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnNone.setOnClickListener(this);

    }

    Button btnBackward, btnPlayPause, btnForward;
    Button btnRepeat, btnShuffle, btnNone;

    final SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss");

    @Override
    public void onClick(View v) {
        if (v == btnBackward) clickBackward();
        else if (v == btnPlayPause) clickPlayPause();
        else if (v == btnForward) clickForward();
        else if (v == btnRepeat) clickRepeat();
        else if (v == btnShuffle) clickShuffle();
        else if (v == btnNone) clickNone();
    }

    public void clickRepeat() {
        isRepeat ^= true;
        String msg = "";
        if(isRepeat) msg = "반복 ON";
        else  msg = "반복 OFF";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void clickShuffle() {
        isShuffle ^= true;
        String msg = "";
        if(isShuffle) msg = "셔플 ON";
        else  msg = "셔플 OFF";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void clickNone() {}

    public void clickBackward() {
        if (isPlaying) btnPlayPause.callOnClick();

        if (!isShuffle && selectPosition <= 0) {
            if (isRepeat)
                selectPosition = musicListStr.size();
            else {
                Toast.makeText(this, "첫번째 노래입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(isShuffle) selectPosition = (int)(Math.random() * musicListStr.size());
        else selectPosition--;

        selectFilePath = musicDir + "/" + musicListStr.get(selectPosition);
        isChanged = true;
        btnPlayPause.callOnClick();
        listView.setItemChecked(selectPosition, true);
    }

    public void clickPlayPause() {
        Log.d("d", "isChanged : " + isChanged);
        Log.d("d", "isPlaying : " + isPlaying);
        if (isChanged && !isPlaying)
            try {
                Log.d("d", "isChanged");
                mp.stop();
                mp.reset();
                mp.setDataSource(selectFilePath);
                Log.d("d", "selectFilePath : " + selectFilePath);
                mp.prepare();
                seekBar.setProgress(0);
                tvTime.setText("00:00");
                seekBar.setMax(mp.getDuration());
                Log.d("d", "mp.getDuration() : " + mp.getDuration());
                isChanged = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        //
        if (isPlaying) {
            Log.i("i", "pause");
            mp.pause();
            isPlaying = false;
            progressBar.setVisibility(View.INVISIBLE);
            btnPlayPause.setBackgroundResource(R.drawable.play);
        } else {
            Log.i("i", "start");
            mp.start();
            isPlaying = true;
            progressBar.setVisibility(View.VISIBLE);
            btnPlayPause.setBackgroundResource(R.drawable.pause);
            timeThread();
        }

    }

    public void clickForward() {
        if (isPlaying) btnPlayPause.callOnClick();

        if (!isShuffle && (selectPosition >= (musicListStr.size() - 1))) {
            if (isRepeat)
                selectPosition = -1;
            else {
                Toast.makeText(this, "마지막 노래입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(isShuffle) selectPosition = (int)(Math.random() * musicListStr.size());
        else selectPosition++;

        selectFilePath = musicDir + "/" + musicListStr.get(selectPosition);
        isChanged = true;
        btnPlayPause.callOnClick();
        listView.setItemChecked(selectPosition, true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("d", "onCompletion");
        Log.d("d", "mp.getDuration : " + mp.getDuration());

        isPlaying = false;
        clickForward();
    }
}
