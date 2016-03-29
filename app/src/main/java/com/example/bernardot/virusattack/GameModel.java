package com.example.bernardot.virusattack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bernardot on 2/10/16.
 */
public class GameModel implements Serializable {
    // Variables for static methods and references of the world from other classes.
    private static double worldWidthStatic;
    private static double worldHeightStatic;
    private static int screenHeightStatic, screenWidthStatic;
    private static PowerUp powerUp;

    // Variables used inside the game model
    private Constants.GameStates gameState;
    private double worldWidth = Constants.worldWidth;
    private double worldHeight;
    private int screenHeight, screenWidth;
    private double ballScaleFactor = Constants.ballScaleFactor;
    private double wallScaleFactor = Constants.wallScaleFactor;
    private int goodClicks;
    private int badClicks;
    private int score;
    private double differenceFictionalReal;
    private boolean portrait;
    private Random generator;

    private Virus virus;
    private List<Wall> walls;
    private int powerUpCounter;
    private int madnessCounter;

    public GameModel(int width, int height, boolean portrait) {
        // Set screen dimensions
        screenWidth = width;
        screenHeight = height;

        this.portrait = portrait;
        if (!this.portrait) {
            // Do some calculation to facilitate future scaling when changing orientation.
            double fictionalWidth = Constants.worldWidth * ((double) screenWidth / (double) screenHeight);
            double fictionalWallScaleFactor = wallScaleFactor * fictionalWidth / Constants.worldWidth;
            double fictionalLeftWallX = fictionalWidth / fictionalWallScaleFactor;
            double fictionalRightWallX = fictionalWidth * (1 - (1 / fictionalWallScaleFactor));
            differenceFictionalReal = (fictionalRightWallX - fictionalLeftWallX) - (Constants.worldWidth * (1 - (1 / wallScaleFactor)) - Constants.worldWidth / wallScaleFactor);

            // Recalculate values to adjust to the landscape orientation.
            worldWidth = worldWidth * ((double) screenWidth / (double) screenHeight);
            ballScaleFactor = ballScaleFactor * worldWidth / Constants.worldWidth;
            wallScaleFactor = wallScaleFactor * worldWidth / Constants.worldWidth;
        } else {
            // Use the portrait given values.
            worldWidth = Constants.worldWidth;
            differenceFictionalReal = 0;
        }

        // Calculate the world height keeping in mind screen width and height
        worldHeight = screenHeight * (worldWidth / screenWidth);

        // Set the static variables for calculations outside this class.
        screenWidthStatic = screenWidth;
        screenHeightStatic = screenHeight;
        worldWidthStatic = worldWidth;
        worldHeightStatic = worldHeight;

        // Set the game state and create objects.
        virus = new Virus(worldWidth / 2, worldHeight / 2 + 1, Constants.ballInitialVelocityX, 0, worldWidth / ballScaleFactor);
        walls = new ArrayList<>();
        walls.add(new Wall(worldWidth / wallScaleFactor + differenceFictionalReal / 2, worldHeight, worldWidth / wallScaleFactor + differenceFictionalReal / 2, 0));
        walls.add(new Wall(worldWidth * (1 - (1 / wallScaleFactor)) - differenceFictionalReal / 2, 0, worldWidth * (1 - (1 / wallScaleFactor)) - differenceFictionalReal / 2, worldHeight));

        gameInit();
    }

    public void gameInit() {
        gameState = Constants.GameStates.MOVING;
        virus.setPosition(new Vector2D(worldWidth / 2, worldHeight / 2 + 1));
        virus.setVelocity(new Vector2D(Constants.ballInitialVelocityX, 0));
        virus.setRadius(worldWidth / ballScaleFactor);
        goodClicks = badClicks = score = 0;
        powerUpCounter = madnessCounter = 0;
        powerUp = new PowerUp();
        generator = new Random();
    }

    public void update() {

        // Check if there is a chance of activating a power up
        if (!gameState.equals(Constants.GameStates.START) && !gameState.equals(Constants.GameStates.END) && !gameState.equals(Constants.GameStates.PAUSED) && powerUpCounter >= powerUp.getDuration()) {
            powerUpCounter = 0;
            if (Constants.monsterMad.isPlaying()) {
                Constants.monsterMad.pause();
            }
            if (powerUp.getType().equals(Constants.PowerUps.NONE)) {
                if (generator.nextDouble() <= Constants.doublePointsProbability) {
                    powerUp = new DoublePoints_PowerUp();
                } else if (generator.nextDouble() <= Constants.reduceSizeProbability) {
                    powerUp = new ReduceSize_PowerUp();
                }
            } else {
                powerUp = new PowerUp();
            }
        }

        // Try to activate madness
        if (score / 50 > madnessCounter && powerUp.getType().equals(Constants.PowerUps.NONE)) {
            System.out.println((goodClicks / (double) (goodClicks + badClicks)));
            if (generator.nextDouble() <= (Constants.madnessProbability - (badClicks / (double) (goodClicks + badClicks)))) {
                powerUp = new Madness_PowerUp();
                if (!Constants.monsterMad.isPlaying()) {
                    Constants.monsterMad.start();
                }
            }
            madnessCounter++;
        }

        // Check if virus penetrated a cell
        if (virus.checkBallInsideLimits(0, worldWidth)) {
            //System.out.println(gameState.toString());
            //System.out.println(virus.getRadius());
            //System.out.println(virus.getVelocity().getX() + "//" + virus.getVelocity().getY());
            gameState = Constants.GameStates.END;
        }

        // Check if virus is touching 2 cells
        checkBallTouchingTwoWalls();

        // Game logic
        if (gameState.equals(Constants.GameStates.MOVING)) {
            virus.updatePosition();
            for (Wall wall : walls) {
                if (wall.isBallCollidingWall(virus.getPosition(), virus.getRadius())) {
                    virus.setVelocity(wall.calculateVelocityAfterACollision(virus.getVelocity()));
                    gameState = Constants.GameStates.COLLISION;
                }
            }
        } else if (gameState.equals(Constants.GameStates.COLLISION)) {
            if (!powerUp.getType().equals(Constants.PowerUps.REDUCE_SIZE)) {
                virus.increaseRadius(1);
                if (!Constants.monsterSuction.isPlaying()) {
                    Constants.monsterSuction.start();
                }
            }
            checkBallTouchingTwoWalls();
        } else if (gameState.equals(Constants.GameStates.CLICK)) {
            boolean touching = false;
            virus.updatePosition();
            for (Wall wall : walls) {
                if (wall.isBallCollidingWall(virus.getPosition(), virus.getRadius())) {
                    touching = true;
                    break;
                }
            }
            if (!touching) {
                gameState = Constants.GameStates.MOVING;
                if (powerUp.getType().equals(Constants.PowerUps.REDUCE_SIZE)) {
                    virus.reduceRadius(5);
                }
            }
            checkBallTouchingTwoWalls();
        }

        if (powerUp.getType().equals(Constants.PowerUps.MADNESS)) {
            virus.increaseRadius(0.4);
        }

        powerUpCounter++;
    }

    public void checkBallTouchingTwoWalls() {
        if (walls.get(0).isBallCollidingWall(virus.getPosition(), virus.getRadius())
                && walls.get(1).isBallCollidingWall(virus.getPosition(), virus.getRadius())) {
            gameState = Constants.GameStates.END;
        }
    }

    public void adjustGameToFitScreen(Virus virus, Double difference, int clicks, Constants.GameStates gameState) {
        setGameState(gameState);
        setGoodClicks(clicks);
        Vector2D ballPosition = this.virus.getPosition();
        this.virus = virus;
        if (difference != null) {
            ballPosition.setX(this.virus.getPosition().getX() - difference / 2);
        } else {
            ballPosition.setX(this.virus.getPosition().getX() + differenceFictionalReal / 2);
        }
        this.virus.setPosition(ballPosition);
    }

    public Virus getVirus() {
        return virus;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public void setGameState(Constants.GameStates gameState) {
        this.gameState = gameState;
    }

    public Constants.GameStates getGameState() {
        return gameState;
    }

    public int getGoodClicks() {
        return goodClicks;
    }

    public void setGoodClicks(int goodClicks) {
        this.goodClicks = goodClicks;
    }

    public int getBadClicks() {
        return badClicks;
    }

    public void setBadClicks(int badClicks) {
        this.badClicks = badClicks;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static int getScreenHeightStatic() {
        return screenHeightStatic;
    }

    public static int getScreenWidthStatic() {
        return screenWidthStatic;
    }

    public boolean isPortrait() {
        return portrait;
    }

    public double getDifferenceFictionalReal() {
        return differenceFictionalReal;
    }

    public static PowerUp getPowerUp() {
        return powerUp;
    }

    // The 3 following methods were taken from the Physics Based Game module
    public static int convertWorldXtoScreenX(double worldX) {
        return (int) (worldX / worldWidthStatic * screenWidthStatic);
    }
    public static int convertWorldYtoScreenY(double worldY) {
        // Minus sign in here is because screen coordinates are upside down.
        return (int) (screenHeightStatic - (worldY / worldHeightStatic * screenHeightStatic));
    }
    public static int convertWorldLengthToScreenLength(double worldLength) {
        return (int) (worldLength / worldWidthStatic * screenWidthStatic);
    }

    public String toString() {
        String model = "";
        model += "Screen: " + screenWidth + ":" + screenHeight + "\n";
        model += "World: " + worldWidth + ":" + worldHeight + "\n";
        model += "Portrait: " + portrait + "\n";
        model += "Virus: " + virus.getPosition().getX() + ":" + virus.getPosition().getY() + "\n";
        model += "Left wall: " + walls.get(0).getStartPosition().getX() + ":" + walls.get(0).getStartPosition().getY()
                + " / " + walls.get(0).getEndPosition().getX() + ":" + walls.get(0).getEndPosition().getY() + "\n";
        model += "Right wall: " + walls.get(1).getStartPosition().getX() + ":" + walls.get(1).getStartPosition().getY()
                + " / " + walls.get(1).getEndPosition().getX() + ":" + walls.get(1).getEndPosition().getY() + "\n";

        return model;
    }
}
