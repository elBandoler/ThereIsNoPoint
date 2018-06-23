package com.gerszkoviez.thereisnopoint;

import android.os.CountDownTimer;

public abstract class GameTimer {

    protected float time = 0;
    protected float interval = 0;
    protected CountDownTimer timer;

    public GameTimer(float time, float interval)
    {
        this.time = time;
        this.interval = interval;
        this.createGameTimer();
    }

    public abstract void TickCD(long currentTime);

    public abstract void FinishCD();

    public void start()
    {
        if(this.timer == null) this.createGameTimer();
        this.timer.start();
    }

    public void cancel()
    {
        this.timer.cancel();
        this.timer = null;
    }

    private void createGameTimer(){
        this.timer = new CountDownTimer((long)(time * 1000),(long) interval) {
            public void onTick(long millisUntilFinished) {
                TickCD(millisUntilFinished);
            }

            public void onFinish() {
                FinishCD();
            }
        };
    }

    public void resetGameTimer(float time)
    {
        this.time = time;
        if(this.timer != null) this.timer.cancel();
        this.createGameTimer();
    }

    public float getStartTime()
    {
        return this.time;
    }

}
