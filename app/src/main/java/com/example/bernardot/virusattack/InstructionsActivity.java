package com.example.bernardot.virusattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;

import java.io.InputStream;

/**
 * Created by bernardot on 2/14/16.
 */
public class InstructionsActivity extends Activity {

    RelativeLayout backButton;
    ImageView imageBackButton;
    TextView textInstructionsView;
    TextView titleInstructionsView;
    TextView textBackView;

    boolean otherActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        backButton = (RelativeLayout) findViewById(R.id.button_menu);
        imageBackButton = (ImageView) findViewById(R.id.img_button);
        textInstructionsView = (TextView) findViewById(R.id.text_instructions);
        titleInstructionsView = (TextView) findViewById(R.id.title_instructions);
        textBackView = (TextView) findViewById(R.id.text_menu);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "Acidic.TTF");
        titleInstructionsView.setTypeface(customFont);
        textBackView.setTypeface(customFont);

        backButton.setOnTouchListener(new ButtonWithTouch(imageBackButton, false));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherActivity = true;
                Intent myIntent = new Intent(InstructionsActivity.this, MenuActivity.class);
                //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        otherActivity = false;
        if (!Constants.backgroundMusic.isPlaying()) {
            try {
                Constants.backgroundMusic.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Constants.backgroundMusic.start();
                    }
                });
                Constants.backgroundMusic.prepareAsync();
            } catch (Exception e) {
                Constants.backgroundMusic.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.backgroundMusic.pause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            otherActivity = true;
            Intent myIntent = new Intent(InstructionsActivity.this, MenuActivity.class);
            //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
