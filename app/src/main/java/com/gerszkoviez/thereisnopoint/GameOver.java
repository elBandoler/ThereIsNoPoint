package com.gerszkoviez.thereisnopoint;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import static android.app.PendingIntent.getActivity;

public class GameOver extends AppCompatActivity {

    protected static int TIME_IS_UP = 0;
    protected static int OUT_OF_LIVES = 1;

    protected MediaPlayer youLoseSound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        youLoseSound = MediaPlayer.create(this, R.raw.wompwomp);
        youLoseSound.start();

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        boolean isBestScore = intent.getBooleanExtra("isBestScore", false);
/*        float bestTime = intent.getFloatExtra("bestTime", 0);
        boolean isBestTime = intent.getBooleanExtra("isBestTime", false);
*/
        TextView yourScoreText = findViewById(R.id.yourScoreText);
        yourScoreText.setText(String.format(Locale.getDefault(),getString(R.string.your_score_is), score));

        if(isBestScore)
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("bestScore", score);
            editor.apply();

            TextView highScoreText = findViewById(R.id.highScoreText);
            highScoreText.setVisibility(View.VISIBLE);
        }

/*        TextView yourTimeText = findViewById(R.id.yourTimeText);
        yourTimeText.setText(String.format(Locale.getDefault(),getString(R.string.your_time_is), bestTime));

        if(isBestTime)
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("bestTime", bestTime);
            editor.apply();

            TextView highScoreText = findViewById(R.id.highScoreText2);
            highScoreText.setVisibility(View.VISIBLE);
        }*/
    }

    /**
     * to block the Back button from returning to the main menu in a stupid way.
     */
    @Override
    public void onBackPressed()
    {
        youLoseSound.stop();
        finish();
    }

    /**
     * called when the No button is pressed
     * returns to the MainActivity by stopping this Activity.
     * @param view
     */
    public void onNoClicked(View view)
    {
        youLoseSound.stop();
        finish();
    }

    /**
     * called when the Yes button is pressed.
     * Starts a new game.
     * @param view
     */
    public void onYesClicked(View view)
    {
        youLoseSound.stop();
        Intent intent = new Intent(GameOver.this, Game.class);
        startActivity(intent);
        finish();
    }
}
