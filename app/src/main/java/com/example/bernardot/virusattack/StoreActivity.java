package com.example.bernardot.virusattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

/**
 * Created by bernardot on 3/26/16.
 */
public class StoreActivity extends Activity {

    RelativeLayout backButton;
    RelativeLayout doubleUpButton;
    RelativeLayout reduceButton;
    RelativeLayout madnessButton;
    ImageView imageBackButton;
    ImageView imageDoubleUpButton;
    ImageView imageReduceButton;
    ImageView imageMadnessButton;
    TextView textBackView;
    TextView textStore;
    TextView textDoubleUpUpgrade;
    TextView textReduceUpgrade;
    TextView textMadnessUpgrade;
    TextView textDoubleUpVitamins;
    TextView textReduceVitamins;
    TextView textMadnessVitamins;
    TextView textVitamins;

    RelativeLayout doubleUpBlueButton;
    RelativeLayout reduceBlueButton;
    RelativeLayout madnessBlueButton;
    ImageView imageBlueDoubleUpButton;
    ImageView imageBlueReduceButton;
    ImageView imageBlueMadnessButton;

    String Level;
    String to;
    String preProbability;
    String postProbability;
    String postMadnessProbability;
    String preSeconds;
    String postSeconds;
    String doubleUpTitle;
    String reduceTitle;
    String madnessTitle;

    boolean otherActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);

        backButton = (RelativeLayout) findViewById(R.id.button_menu);
        doubleUpButton = (RelativeLayout) findViewById(R.id.button_doubleUp);
        reduceButton = (RelativeLayout) findViewById(R.id.button_reduce);
        madnessButton = (RelativeLayout) findViewById(R.id.button_madness);
        doubleUpBlueButton = (RelativeLayout) findViewById(R.id.doublePoints_box);
        reduceBlueButton = (RelativeLayout) findViewById(R.id.reduce_box);
        madnessBlueButton = (RelativeLayout) findViewById(R.id.madness_box);
        imageBackButton = (ImageView) findViewById(R.id.img_button);
        imageDoubleUpButton = (ImageView) findViewById(R.id.imgDoublePoints_upgrade_box);
        imageReduceButton = (ImageView) findViewById(R.id.imgReduce_upgrade_box);
        imageMadnessButton = (ImageView) findViewById(R.id.imgMadness_upgrade_box);
        imageBlueDoubleUpButton = (ImageView) findViewById(R.id.imgDoublePoints_box);
        imageBlueReduceButton = (ImageView) findViewById(R.id.imgReduce_box);
        imageBlueMadnessButton = (ImageView) findViewById(R.id.imgMadness_box);
        textBackView = (TextView) findViewById(R.id.text_menu);
        textStore = (TextView) findViewById(R.id.store_title);
        textDoubleUpUpgrade = (TextView) findViewById(R.id.store_upgrade_doublePoints);
        textReduceUpgrade = (TextView) findViewById(R.id.store_upgrade_reduce);
        textMadnessUpgrade = (TextView) findViewById(R.id.store_upgrade_madness);
        textDoubleUpVitamins = (TextView) findViewById(R.id.vitamins_upgrade_doublePoints);
        textReduceVitamins = (TextView) findViewById(R.id.vitamins_upgrade_reduce);
        textMadnessVitamins = (TextView) findViewById(R.id.vitamins_upgrade_madness);
        textVitamins = (TextView) findViewById(R.id.text_vitamin);
        textVitamins.setText(String.valueOf(loadVitamins()));

        Typeface customFont = Typeface.createFromAsset(getAssets(), "Acidic.TTF");
        textBackView.setTypeface(customFont);
        textStore.setTypeface(customFont);
        textDoubleUpUpgrade.setTypeface(customFont);
        textReduceUpgrade.setTypeface(customFont);
        textMadnessUpgrade.setTypeface(customFont);

        Level = getResources().getString(R.string.LevelMessage);
        to = getResources().getString(R.string.toUpgradeMessage);
        preProbability = getResources().getString(R.string.probabilityMessage);
        postProbability = getResources().getString(R.string.secondMessage);
        preSeconds = getResources().getString(R.string.timeMessage);
        postSeconds = getResources().getString(R.string.secondsMessage);
        postMadnessProbability = getResources().getString(R.string.postProbabilityMessage);
        doubleUpTitle = getResources().getString(R.string.doubleUpTitle);
        reduceTitle = getResources().getString(R.string.reduceTitle);
        madnessTitle = getResources().getString(R.string.madnessTitle);

        backButton.setOnTouchListener(new ButtonWithTouch(imageBackButton, false));
        doubleUpButton.setOnTouchListener(new ButtonWithTouch(imageDoubleUpButton, false));
        reduceButton.setOnTouchListener(new ButtonWithTouch(imageReduceButton, false));
        madnessButton.setOnTouchListener(new ButtonWithTouch(imageMadnessButton, false));
        doubleUpBlueButton.setOnTouchListener(new ButtonWithTouch(imageBlueDoubleUpButton, true));
        reduceBlueButton.setOnTouchListener(new ButtonWithTouch(imageBlueReduceButton, true));
        madnessBlueButton.setOnTouchListener(new ButtonWithTouch(imageBlueMadnessButton, true));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherActivity = true;
                Intent myIntent = new Intent(StoreActivity.this, MenuActivity.class);
                //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }
        });

        doubleUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vitamins = loadVitamins();
                if (vitamins >= Constants.doublePointsVitamins) {
                    saveVitamins(-Constants.doublePointsVitamins);
                    textVitamins.setText(String.valueOf(loadVitamins()));

                    Constants.doublePointsVitamins = (int) ((Constants.doublePointsVitamins * 1.9 - Constants.doublePointsLevel * Math.pow(1.2, Constants.doublePointsLevel)) / 10.0) * 10;
                    textDoubleUpVitamins.setText(String.valueOf(Constants.doublePointsVitamins));
                    Constants.doublePointsTime += Constants.increseDoublePointsTime;
                    Constants.doublePointsProbability += Constants.increaseProbabilityDoublePoints;
                    Constants.doublePointsLevel++;
                }
            }
        });

        doubleUpBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(StoreActivity.this).create();
                alertDialog.setTitle(doubleUpTitle);
                alertDialog.setMessage(Level + " " + Constants.doublePointsLevel + " " + to + " " + Level + " " + (Constants.doublePointsLevel + 1) + "\n"
                        + preProbability + " " + new DecimalFormat("#.00").format(Constants.doublePointsProbability * 100) + "% " + to + " " + new DecimalFormat("#.00").format((Constants.doublePointsProbability + Constants.increaseProbabilityDoublePoints) * 100) + "% " + postProbability + "\n"
                        + preSeconds + " " + (Constants.doublePointsTime / 50.0) + " " + to + " " + ((Constants.doublePointsTime + Constants.increseDoublePointsTime) / 50.0) + " " + postSeconds);

                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                        "OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });

        reduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vitamins = loadVitamins();
                if (vitamins >= Constants.reduceVitamins) {
                    saveVitamins(-Constants.reduceVitamins);
                    textVitamins.setText(String.valueOf(loadVitamins()));

                    Constants.reduceVitamins = (int) ((Constants.reduceVitamins * 1.8 - Constants.reduceLevel * Math.pow(1.1, Constants.reduceLevel)) / 10.0) * 10;
                    textReduceVitamins.setText(String.valueOf(Constants.reduceVitamins));
                    Constants.reduceSizeTime += Constants.increaseReduceSizeTime;
                    Constants.reduceSizeProbability += Constants.increaseProbabilityReduce;
                    Constants.reduceLevel++;
                }
            }
        });

        reduceBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(StoreActivity.this).create();
                alertDialog.setTitle(reduceTitle);
                alertDialog.setMessage(Level + " " + Constants.reduceLevel + " " + to + " " + Level + " " + (Constants.reduceLevel + 1) + "\n"
                        + preProbability + " " + new DecimalFormat("#.00").format(Constants.reduceSizeProbability * 100) + "% " + to + " " + new DecimalFormat("#.00").format((Constants.reduceSizeProbability + Constants.increaseProbabilityReduce) * 100) + "% " + postProbability + "\n"
                        + preSeconds + " " + (Constants.reduceSizeTime / 50.0) + " " + to + " " + ((Constants.reduceSizeTime + Constants.increaseReduceSizeTime) / 50.0) + " " + postSeconds);

                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                        "OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });

        madnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vitamins = loadVitamins();
                if (vitamins >= Constants.madnessVitamins) {
                    saveVitamins(-Constants.madnessVitamins);
                    textVitamins.setText(String.valueOf(loadVitamins()));

                    Constants.madnessVitamins = (int) ((Constants.madnessVitamins * 1.7 - Constants.madnessLevel * Math.pow(1.05, Constants.madnessLevel)) / 10.0) * 10;
                    textMadnessVitamins.setText(String.valueOf(Constants.madnessVitamins));
                    Constants.madnessTime += Constants.increaseMadnessTime;
                    Constants.madnessProbability += Constants.increaseProbabilityMadness;
                    Constants.madnessLevel++;
                }
            }
        });

        madnessBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(StoreActivity.this).create();
                alertDialog.setTitle(madnessTitle);
                alertDialog.setMessage(Level + " " + Constants.madnessLevel + " " + to + " " + Level + " " + (Constants.madnessLevel + 1) + "\n"
                        + preProbability + " " + new DecimalFormat("#.00").format(Constants.madnessProbability * 100) + "% " + to + " " + new DecimalFormat("#.00").format((Constants.madnessProbability + Constants.increaseProbabilityMadness) * 100) + "% " + postMadnessProbability + "\n"
                        + preSeconds + " " + (Constants.madnessTime / 50.0) + " " + to + " " + ((Constants.madnessTime + Constants.increaseMadnessTime) / 50.0) + " " + postSeconds);

                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                        "OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        otherActivity = true;
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

        //resetUpgrades();
        loadUpgrades();
        textDoubleUpVitamins.setText(String.valueOf(Constants.doublePointsVitamins));
        textReduceVitamins.setText(String.valueOf(Constants.reduceVitamins));
        textMadnessVitamins.setText(String.valueOf(Constants.madnessVitamins));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.backgroundMusic.pause();

        saveUpgrades();
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

    public void loadUpgrades() {
        try {
            FileInputStream in = openFileInput(Constants.upgradeSavedFile);
            ObjectInputStream obj = new ObjectInputStream(in);
            Constants.doublePointsProbability = obj.readDouble();
            Constants.reduceSizeProbability = obj.readDouble();
            Constants.madnessProbability = obj.readDouble();

            Constants.doublePointsTime = obj.readInt();
            Constants.reduceSizeTime = obj.readInt();
            Constants.madnessTime = obj.readInt();

            Constants.doublePointsVitamins = obj.readInt();
            Constants.reduceVitamins = obj.readInt();
            Constants.madnessVitamins = obj.readInt();

            Constants.doublePointsLevel = obj.readInt();
            Constants.reduceLevel = obj.readInt();
            Constants.madnessLevel = obj.readInt();
            obj.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(Constants.doublePointsLevel);
        //System.out.println(Constants.reduceLevel);
        //System.out.println(Constants.madnessLevel);
    }

    public void saveUpgrades() {
        try {
            FileOutputStream out = openFileOutput(Constants.upgradeSavedFile, Context.MODE_PRIVATE);
            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeDouble(Constants.doublePointsProbability);
            obj.writeDouble(Constants.reduceSizeProbability);
            obj.writeDouble(Constants.madnessProbability);

            obj.writeInt(Constants.doublePointsTime);
            obj.writeInt(Constants.reduceSizeTime);
            obj.writeInt(Constants.madnessTime);

            obj.writeInt(Constants.doublePointsVitamins);
            obj.writeInt(Constants.reduceVitamins);
            obj.writeInt(Constants.madnessVitamins);

            obj.writeInt(Constants.doublePointsLevel);
            obj.writeInt(Constants.reduceLevel);
            obj.writeInt(Constants.madnessLevel);
            obj.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUpgrades() {
        try {
            FileOutputStream out = openFileOutput(Constants.upgradeSavedFile, Context.MODE_PRIVATE);
            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeDouble(Constants.doublePointsProbability);
            obj.writeDouble(Constants.reduceSizeProbability);
            obj.writeDouble(Constants.madnessProbability);

            obj.writeInt(Constants.doublePointsTime);
            obj.writeInt(Constants.reduceSizeTime);
            obj.writeInt(Constants.madnessTime);

            obj.writeInt(Constants.doublePointsVitamins);
            obj.writeInt(Constants.reduceVitamins);
            obj.writeInt(Constants.madnessVitamins);

            obj.writeInt(Constants.doublePointsLevel);
            obj.writeInt(Constants.reduceLevel);
            obj.writeInt(Constants.madnessLevel);
            obj.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            otherActivity = true;
            Intent myIntent = new Intent(StoreActivity.this, MenuActivity.class);
            //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
