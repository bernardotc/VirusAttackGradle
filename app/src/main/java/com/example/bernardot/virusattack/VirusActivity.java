package com.example.bernardot.virusattack;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.*;
import java.text.DecimalFormat;

public class VirusActivity extends Activity {

    GameModel model;
    GameView view;
    GameThread game;
    DisplayMetrics metrics;
    boolean portrait = true;
    boolean otherActivity = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        // Create sounds
        Constants.monsterSuction = MediaPlayer.create(getApplicationContext(), R.raw.monster_suction);
        Constants.monsterSuction.setLooping(true);
        Constants.monsterMad = MediaPlayer.create(getApplicationContext(), R.raw.monster_mad);
        Constants.monsterMad.setLooping(true);
        Constants.click = MediaPlayer.create(getApplicationContext(), R.raw.click_sound);

        // Get screen resolution
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Create model object
        portrait = true;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            portrait = false;
        }
        model = new GameModel(metrics.widthPixels, metrics.heightPixels, portrait);

        // Set String and size constants of the app
        setStringSizeConstants();

        // Create view object
        view = new GameView(this);

        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        otherActivity = false;
        //setContentView(view);
        if (!Constants.backgroundMusic.isPlaying() || model.getGameState().equals(Constants.GameStates.END)) {
            Constants.backgroundMusic.start();
            loadGame();
        } else {
            model.gameInit();
            setContentView(view);
        }
        game = new GameThread();
        game.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.backgroundMusic.pause();
        //Constants.monsterSuction.release();
        //Constants.monsterMad.release();
        if (!getModel().getGameState().equals(Constants.GameStates.END) && !getModel().getGameState().equals(Constants.GameStates.START)) {
            getModel().setGameState(Constants.GameStates.PAUSED);
        }
        saveGame();
        getModel().setGameState(Constants.GameStates.STOP);
    }

    public void saveGame() {
        try {
            FileOutputStream out = openFileOutput(Constants.saveFile, Context.MODE_PRIVATE);
            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeObject(getModel());
            obj.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try {
            FileInputStream in = openFileInput(Constants.saveFile);
            ObjectInputStream obj = new ObjectInputStream(in);
            GameModel game = (GameModel) obj.readObject();
            loadGameDataToModel(game);
            obj.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public int loadVitamins() {
        int vitamins = 0;
        try {
            FileInputStream in = openFileInput(Constants.vitaminsSavedFile);
            ObjectInputStream obj = new ObjectInputStream(in);
            vitamins = obj.readInt();
            //System.out.println(vitamins);
            obj.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vitamins;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.modelKey, getModel());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GameModel game = (GameModel) savedInstanceState.getSerializable(Constants.modelKey);
        loadGameDataToModel(game);
    }

    public GameModel getModel() {
        return model;
    }
    
    public void loadGameDataToModel(GameModel game) {
        if (!game.isPortrait() && portrait) {
            getModel().adjustGameToFitScreen(game.getVirus(), game.getDifferenceFictionalReal(), game.getGoodClicks(), game.getGameState());
        } else if (game.isPortrait() && !portrait) {
            getModel().adjustGameToFitScreen(game.getVirus(), null, game.getGoodClicks(), game.getGameState());
        } else {
            model = game;
        }
        //System.out.println("l " + model.getVirus().getVelocity().getX());
    }

    class GameThread extends Thread {
        boolean running = true;

        public void run() {
            while (running) {
                try {
                    getModel().update();
                    if (getModel().getGameState().equals(Constants.GameStates.END)) {
                        if (Constants.monsterMad.isPlaying()) {
                            Constants.monsterMad.pause();
                        }
                        if (Constants.monsterSuction.isPlaying()) {
                            Constants.monsterSuction.pause();
                        }
                        loadShowScore();
                        //System.out.println("Joining thread");
                        join();
                    } else if (getModel().getGameState().equals(Constants.GameStates.STOP)) {
                        if (Constants.monsterMad.isPlaying()) {
                            Constants.monsterMad.pause();
                        }
                        if (Constants.monsterSuction.isPlaying()) {
                            Constants.monsterSuction.pause();
                        }
                        //System.out.println("Joining thread");
                        join();
                    } else {
                        view.draw();
                    }
                    Thread.sleep(Constants.delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getResources().getString(R.string.instructionsItem));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getResources().getString(R.string.instructionsItem))) {
            Intent intent = new Intent(Intent.ACTION_ASSIST);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !getModel().getGameState().equals(Constants.GameStates.END)) {
            // Pause the game
            if (!getModel().getGameState().equals(Constants.GameStates.END) && !getModel().getGameState().equals(Constants.GameStates.START))
                getModel().setGameState(Constants.GameStates.PAUSED);

            // Then, continue with the dialog.
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(Constants.backTitleDialog);
            alertDialog.setMessage(Constants.backMessageDialog);

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    Constants.backMessagePositive, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getModel().setGameState(Constants.GameStates.STOP);
                            otherActivity = true;
                            Intent myIntent = new Intent(VirusActivity.this, MenuActivity.class);
                            //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(myIntent);
                        }
                    });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    Constants.backMessageNegative, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing dialog will dismiss
                        }
                    });
            alertDialog.show();
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            otherActivity = true;
            Intent myIntent = new Intent(VirusActivity.this, MenuActivity.class);
            //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setStringSizeConstants() {
        Constants.modelKey = getResources().getString(R.string.modelKey);
        Constants.saveFile = getResources().getString(R.string.saveFile);

        Constants.backTitleDialog = getResources().getString(R.string.backTitleDialog);
        Constants.backMessageDialog = getResources().getString(R.string.backMessageDialog);
        Constants.backMessagePositive = getResources().getString(R.string.backMessagePositive);
        Constants.backMessageNegative = getResources().getString(R.string.backMessageNegative);
        Constants.pauseMessage = getResources().getString(R.string.pausedMessage);
        Constants.endMessage = getResources().getString(R.string.endMessage);
        Constants.titleMessage = getResources().getString(R.string.titleMessage);
        Constants.resumeMessage = getResources().getString(R.string.resumeMessage);
        Constants.restartMessage = getResources().getString(R.string.restartMessage);
        Constants.startMessage = getResources().getString(R.string.startMessage);
        Constants.doubleClicksMessage = getResources().getString(R.string.doubleClicksMessage);
        Constants.clickMessage = getResources().getString(R.string.clickMessage);
        Constants.reduceMessage = getResources().getString(R.string.reduceMessage);
        Constants.madnessMessage = getResources().getString(R.string.madnessMessage);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Constants.scorePaint.setTextSize(GameModel.getScreenWidthStatic() / 30);
            Constants.infoPaint.setTextSize(GameModel.getScreenWidthStatic() / 15);
        } else {
            Constants.scorePaint.setTextSize(GameModel.getScreenWidthStatic() / 20);
            Constants.infoPaint.setTextSize(GameModel.getScreenWidthStatic() / 10);
        }

        Typeface customFont = Typeface.createFromAsset(getAssets(), "Acidic.TTF");
        Constants.infoPaint.setTypeface(customFont);
    }

    public void loadShowScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout backMenuButton;
                ImageView imageBackMenyButton;
                TextView textScore;
                TextView textGoodClicks;
                TextView textBadClicks;
                TextView textAccuracy;
                TextView textNewVitamins;
                TextView textBackMenuView;
                TextView titleView;

                setContentView(R.layout.score);
                backMenuButton = (RelativeLayout) findViewById(R.id.button_menu);
                imageBackMenyButton = (ImageView) findViewById(R.id.img_button);
                textBackMenuView = (TextView) findViewById(R.id.text_menu);
                textScore = (TextView) findViewById(R.id.score_game);
                textGoodClicks = (TextView) findViewById(R.id.goodClicks_game);
                textBadClicks = (TextView) findViewById(R.id.badClicks_game);
                textAccuracy = (TextView) findViewById(R.id.accuracy_game);
                textNewVitamins = (TextView) findViewById(R.id.totalVitamins_game);
                titleView = (TextView) findViewById(R.id.title_game);
                Typeface customFont = Typeface.createFromAsset(getAssets(), "Acidic.TTF");
                textBackMenuView.setTypeface(customFont);
                titleView.setTypeface(customFont);
                textScore.setTypeface(customFont);
                textGoodClicks.setTypeface(customFont);
                textBadClicks.setTypeface(customFont);
                textAccuracy.setTypeface(customFont);
                textNewVitamins.setTypeface(customFont);

                textScore.setText(textScore.getText() + " " + getModel().getScore());
                textGoodClicks.setText(textGoodClicks.getText() + " " + getModel().getGoodClicks());
                textBadClicks.setText(textBadClicks.getText() + " " + getModel().getBadClicks());
                Double accuracy = getModel().getGoodClicks() / (double) (getModel().getGoodClicks() + getModel().getBadClicks()) * 100;
                if (accuracy.isNaN()) {
                    accuracy = 0.0;
                }
                textAccuracy.setText(textAccuracy.getText() + " " + new DecimalFormat("#.00").format(accuracy) + "%");
                int newVitamins = (int) (getModel().getScore() * accuracy / 100);
                textNewVitamins.setText(textNewVitamins.getText() + " " + newVitamins);
                saveVitamins(newVitamins);

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentTitle("Playing Virus Attack!")
                        .setContentDescription("My score was " + getModel().getScore() + ". Can you surpass it?")
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
                shareButton.setShareContent(content);

                backMenuButton.setOnTouchListener(new ButtonWithTouch(imageBackMenyButton, false));

                backMenuButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        otherActivity = true;
                        Intent myIntent = new Intent(VirusActivity.this, MenuActivity.class);
                        //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(myIntent);
                    }
                });
            }
        });
    }
}
