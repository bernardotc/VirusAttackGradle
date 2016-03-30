package com.example.bernardot.virusattack;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by bernardot on 3/23/16.
 */
public class MenuActivity extends Activity {

    RelativeLayout startButton;
    ImageView imageStartButton;
    TextView textStartView;
    TextView titleView;
    TextView vitaminsView;

    RelativeLayout storeButton;
    ImageView imageStoreButton;
    TextView textStoreView;

    RelativeLayout instructionsButton;
    ImageView imageInstructionsButton;
    TextView textInstructionsView;

    boolean otherActivity = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.main);

        Constants.backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.background_sound);
        Constants.backgroundMusic.setLooping(true);
        Constants.vitaminsSavedFile = getResources().getString(R.string.saveVitaminsFile);
        Constants.upgradeSavedFile = getResources().getString(R.string.upgradesFile);

        startButton = (RelativeLayout) findViewById(R.id.button_start);
        imageStartButton = (ImageView) findViewById(R.id.img_button);
        textStartView = (TextView) findViewById(R.id.text_start);
        titleView = (TextView) findViewById(R.id.title_game);

        instructionsButton = (RelativeLayout) findViewById(R.id.button_instructions);
        imageInstructionsButton = (ImageView) findViewById(R.id.img_buttonInstructions);
        textInstructionsView = (TextView) findViewById(R.id.text_instructions);

        storeButton = (RelativeLayout) findViewById(R.id.button_store);
        imageStoreButton = (ImageView) findViewById(R.id.img_buttonStore);
        textStoreView = (TextView) findViewById(R.id.text_store);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "Acidic.TTF");
        textStartView.setTypeface(customFont);
        titleView.setTypeface(customFont);
        textInstructionsView.setTypeface(customFont);
        textStoreView.setTypeface(customFont);

        startButton.setOnTouchListener(new ButtonWithTouch(imageStartButton, false));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherActivity = true;
                Intent myIntent = new Intent(MenuActivity.this, VirusActivity.class);
                //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }
        });

        instructionsButton.setOnTouchListener(new ButtonWithTouch(imageInstructionsButton, false));

        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherActivity = true;
                Intent myIntent = new Intent(MenuActivity.this, InstructionsActivity.class);
                //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }
        });

        storeButton.setOnTouchListener(new ButtonWithTouch(imageStoreButton, false));

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherActivity = true;
                Intent myIntent = new Intent(MenuActivity.this, StoreActivity.class);
                //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Constants.close = false;
        vitaminsView = (TextView) findViewById(R.id.text_vitamin);
        //saveVitamins(500000);
        int vitamins = loadVitamins();
        vitaminsView.setText(String.valueOf(vitamins));
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
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!otherActivity) {
            Constants.backgroundMusic.pause();
            AppEventsLogger.deactivateApp(this);
        }
    }

    public int loadVitamins() {
        int vitamins = 0;
        try {
            FileInputStream in = openFileInput(Constants.vitaminsSavedFile);
            ObjectInputStream obj = new ObjectInputStream(in);
            vitamins = obj.readInt();
            obj.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vitamins;
    }

    public void saveVitamins(int additionalVitamins) {
        int vitamins = loadVitamins();
        vitamins += additionalVitamins;
        try {
            FileOutputStream out = openFileOutput(Constants.vitaminsSavedFile, Context.MODE_PRIVATE);
            ObjectOutputStream obj = new ObjectOutputStream(out);
            //System.out.println(vitamins);
            obj.writeInt(vitamins);
            obj.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Constants.close = true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
