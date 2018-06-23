package com.gerszkoviez.thereisnopoint;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/*
    versionCode 1524 = Version 1 on 24/5/18 night (although it was 6am!!!!!!!111)
    Beta: ended! [build 1525]
    RC1: ended! [build 1527]
    RC2: ended! [build 1612]
    RC3: current [build 1615]

    RC4: [build 1630 max.]
    BACKUP FIRST!
    add settings
    add credits screen:
        idea - Amit_B @ Oversight Group
        programming - Bandoler @ Gerszkoviez
        sounds - http://www.orangefreesounds.com/
        design - Bandoler @ Gerszkoviez


    1.0: [build 1701 max.]
    redesign app as much as possible: fonts, texts, colors, branding
    replace game over screen with a dialog over the Game screen.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startGame(View view)
    {
        Intent intent = new Intent(MainActivity.this, Game.class);
        startActivity(intent);
    }
}
