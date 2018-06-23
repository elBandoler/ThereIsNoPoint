package com.gerszkoviez.thereisnopoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


public class Game extends AppCompatActivity {

    //variables that are needed!
    protected int score = 0;
    protected int lives = 3;
    protected float timeLeft = 3f; //in secs
    protected int circleSize = 75;
    protected int bestScore = 0;
//    protected float bestTime = 0f;
//    protected float bestTimeHighscore = 0f;
    protected float currentStartingTime = timeLeft;
    protected boolean isBestTime = false;

    protected DisplayMetrics metrics; // needed for screen measures
    protected SharedPreferences sharedPref; //needed for data

    //sounds
    protected MediaPlayer boeing;
    protected MediaPlayer breakingGlass;

    //countdown clock
    protected TextView timerText;
    public GameTimer timer = new GameTimer(timeLeft, 100) {

        @Override
        public void TickCD(long currentTime) {
            timeLeft -= 0.1;
            //Log.i("Data:", "" + timeleft + " general: " + currentStartingTime);
            setTime(currentTime);
        }

        @Override
        public void FinishCD() {
            if(lives > 0) {
                lives--;
                updateLives();

                randomizeCircleSize((ImageView)findViewById(R.id.circleImage));
                setTheTimeValue();
                timer.start(); //start the timer
                setCircleInRandomPosition(findViewById(R.id.circleImage)); //move that circle to somewhere else
            }
            else {
                timer.cancel();
            }
        }
    };

    /**
     * sets the time text in a seconds.deciseconds format, based on the millisUntilFinished
     * @param millisUntilFinished - time to finish in milliseconds
     */
    private void setTime(long millisUntilFinished) {
        int seconds = (int) millisUntilFinished / 1000;
        int deciseconds = (int) (millisUntilFinished / 100) - (seconds * 10);
        timerText.setText(String.format(Locale.getDefault(),"%d.%d", seconds, deciseconds));
    }

    /**
     * called on the activity's creation and defines or starts the basics:
     * a CountdownTimer
     * a DisplayMetrics
     * @param savedInstanceState - default Bundle
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Getting the metrics to be able to relocate the circle
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        boeing = MediaPlayer.create(this, R.raw.boeing);
        breakingGlass = MediaPlayer.create(this, R.raw.breakingglass);

        timeLeft = 3;
        timerText = findViewById(R.id.timerText);
        timer.start();

        //for reading data
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //read best score
        setBestScore(sharedPref.getInt("bestScore", 0));
/*        setBestTime(sharedPref.getFloat("bestTime", 0));
        bestTimeHighscore = bestTime;*/
        isBestTime = false;
   }

    /**
     * called when the circle is clicked on.
     * adds the needed score and regenerates the core mechanics.
     * @param view - the circle as View
     */
    public void onClickCircle(View view)
    {
        score++; //add a point
        updateScoreText(); //make that point visible

/*        float reactionTime = (currentStartingTime - timeLeft);
        if(reactionTime < bestTime || bestTime == 0)
        {
            setBestTime(reactionTime);
        }*/
        timer.cancel(); //stop the timer


        if(boeing.isPlaying())
        {
            boeing.seekTo(0);
        }
        else {
            boeing.start();
        }
        randomizeCircleSize((ImageView) view);
        setCircleInRandomPosition(view); //move that circle to somewhere else
        setTheTimeValue(); // give more time for the next circle
        if(score > bestScore) { setBestScore(score); }
        timer.start(); //start the timer
    }

    /**
     * called when the circle is clicked on.
     * removes a life and stops the game if needed
     * @param view - the circle as View
     */
    public void onClickBoard(View view)
    {
        lives--;
        if(breakingGlass.isPlaying())
        {
            breakingGlass.seekTo(0);
        }
        else {
            breakingGlass.start();
        }
        updateLives();
    }

    /**
     * updates the Score text
     */
    private void updateScoreText()
    {
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText(String.format(Locale.getDefault(),"%d", score));
    }

    /**
     * updates the Lives image
     */
    private void updateLives()
    {
        ImageView livesImage = findViewById(R.id.livesImage);
        int source = R.drawable.threehearts;
        switch(lives)
        {
            case 0:
            {
                source = R.drawable.nohearts;
                stopGame();
                break;
            }
            case 1:
            {
                source = R.drawable.oneheart;
                break;
            }
            case 2:
            {
                source = R.drawable.twohearts;
                break;
            }
        }
        livesImage.setImageResource(source);
    }

    /**
     * sets the circle (basically any object) in a randomly generated position on the screen, except the top score-time area.
     * @param view - the circle as View
     */
    private void setCircleInRandomPosition(View view) {
        float gameWidth = metrics.widthPixels;
        float gameHeight = metrics.heightPixels;
        float topBar = (metrics.density * 65f);
        float circleWidth = view.getWidth();
        float circleHeight = view.getHeight();

        float upper = (gameHeight - (2 * circleHeight));
        float lower = (topBar + circleHeight);

        float newX = (float) (Math.random() * (gameWidth - circleWidth)) + (circleWidth * (5/8));
        float newY = (float) (Math.random() * (upper - lower)) + lower;

        //Log.i("", " " + newX + " " + newY);
        //Log.i("", " " + circleWidth + " " + circleHeight + " " + topBar + " " + gameWidth +  " " + gameHeight + " " + metrics.density);

        view.setX(newX);
        view.setY(newY);
    }

    /**
     * Randomizes the circle's size.
     * @param view - the ImageView of the circle
     */
    private void randomizeCircleSize(ImageView view)
    {
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
        circleSize = (int) ((Math.random() * (dp(100) - dp(25))) + dp(25));
        params.height = circleSize;
        params.width = circleSize;
    }

    private void setTheTimeValue()
    {
        int circleSizeDP = (int) undp(circleSize);
        //Log.i("", " " + circleSizeDP + " " + circleSize);
        if(circleSizeDP > 80)
        {
            timeLeft = 1f;
        }
        else if(circleSizeDP > 50)
        {
            timeLeft = 2f;
        }
        else
        {
            timeLeft = 3f;
        }

        currentStartingTime = timeLeft;

        timer.resetGameTimer(timeLeft);
    }

    /**
     * stops the game, shows a 'Game Over' screen.
     */
    private void stopGame() {
        boeing.stop();
        timer.cancel();
        findViewById(R.id.circleImage).setVisibility(View.INVISIBLE);
        Intent intent = new Intent(Game.this, GameOver.class);
        intent.putExtra("score", score);
        intent.putExtra("isBestScore", (score == bestScore));
//        intent.putExtra("bestTime", bestTime);
//        intent.putExtra("isBestTime", isBestTime);
        startActivity(intent);
        finish();
    }

    /**
     * to block the Back button from returning to the main menu in a stupid way.
     */
    @Override
    public void onBackPressed() {
        breakingGlass.stop();
        boeing.stop();
        timer.cancel();
        finish();
    }

    public void setBestScore(int score) {
        bestScore = score;
        TextView bestScoreText = findViewById(R.id.bestScoreText);
        bestScoreText.setText(String.format(Locale.getDefault(),getString(R.string.default_bestScore), bestScore));
    }

/*    public void setBestTime(float newBestTime) {
        bestTime = newBestTime;
        if(bestTime > bestTimeHighscore) isBestTime = true;
        TextView bestScoreText = findViewById(R.id.bestTimeText);
        bestScoreText.setText(String.format(Locale.getDefault(),getString(R.string.default_bestTime), (float) ((int)(newBestTime * 100)) / 100));
    }*/


    /**
     * translates a float number that represents dp into a relative-to-the-density value
     * @param n represented dp value
     * @return the relative value
     */
    public float dp(float n)
    {
        return n * metrics.density;
    }

    public float undp(float dp)
    {
        return dp / metrics.density;
    }
}
