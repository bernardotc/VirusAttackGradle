package com.example.bernardot.virusattack;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

/**
 * Created by bernardot on 2/10/16.
 */
public class Constants {
    // App constants
    static MediaPlayer backgroundMusic;
    static String vitaminsSavedFile;
    static String upgradeSavedFile;
    static boolean close = false;

    // VirusActivty Constants
    static String modelKey;
    static String saveFile;
    static int delay = 20;

    static String backTitleDialog, backMessageDialog, backMessagePositive, backMessageNegative;

    // GameView Constants
    static String pauseMessage, endMessage, titleMessage, resumeMessage, restartMessage, startMessage, doubleClicksMessage, clickMessage, reduceMessage, madnessMessage;

    static Paint paintBall, sickPaintBall, doublePaintBall, madnessPaintBall, paintWall, scorePaint, infoPaint;

    static {
        paintBall = new Paint();
        paintBall.setColor(Color.BLUE);
        paintBall.setStyle(Paint.Style.STROKE);
        paintBall.setAntiAlias(true);

        sickPaintBall = new Paint();
        sickPaintBall.setColor(Color.MAGENTA);
        sickPaintBall.setStyle(Paint.Style.STROKE);
        sickPaintBall.setAntiAlias(true);

        doublePaintBall = new Paint();
        doublePaintBall.setColor(Color.rgb(219, 237, 126));
        doublePaintBall.setStyle(Paint.Style.STROKE);
        doublePaintBall.setAntiAlias(true);

        madnessPaintBall = new Paint();
        madnessPaintBall.setColor(Color.RED);
        madnessPaintBall.setStyle(Paint.Style.STROKE);
        madnessPaintBall.setAntiAlias(true);

        paintWall = new Paint();
        paintWall.setColor(Color.WHITE);
        paintWall.setStyle(Paint.Style.FILL);
        paintWall.setAntiAlias(true);

        scorePaint = new Paint();
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setColor(Color.YELLOW);
        scorePaint.setAntiAlias(true);

        infoPaint = new Paint();
        infoPaint.setStyle(Paint.Style.FILL);
        infoPaint.setColor(Color.rgb(255, 165, 0));
        infoPaint.setAntiAlias(true);
    }

    // GameModel Constants
    enum GameStates {
        COLLISION, MOVING, CLICK, END, PAUSED, START, STOP
    }

    static int simpleClick = 1;
    static int doublePointClick = 2;

    static MediaPlayer monsterSuction;
    static MediaPlayer monsterMad;
    static MediaPlayer click;

    static double worldWidth = 10;
    static double ballScaleFactor = 20;
    static double wallScaleFactor = 16;
    static double ballInitialVelocityX = 8;

    // PowerUps Constants
    enum PowerUps {
        DOUBLE_POINTS, REDUCE_SIZE, MADNESS, NONE
    }
    static double doublePointsProbability = .02;
    static double reduceSizeProbability = .01;
    static double madnessProbability = .7;

    static int doublePointsTime = 250;
    static int reduceSizeTime = 250;
    static int madnessTime = 200;

    static int doublePointsVitamins = 50;
    static int reduceVitamins = 100;
    static int madnessVitamins = 500;

    static int doublePointsLevel = 1;
    static int reduceLevel = 1;
    static int madnessLevel = 1;

    static double increaseProbabilityDoublePoints = 0.001;
    static double increaseProbabilityReduce = 0.001;
    static double increaseProbabilityMadness = -0.005;

    static int increseDoublePointsTime = 20;
    static int increaseReduceSizeTime = 15;
    static int increaseMadnessTime = -2;

    // Virus Constants
    static double increaseRadiusFactor = 0.01;
    static double reduceRadiusFactor = 0.002;
    static double deltaTime = 0.01;

    // Wall Constants
    static double coefficientOfRestitution = 1.005;
}
