package com.example.bernardot.virusattack;

import android.graphics.*;

import java.io.Serializable;

/**
 * Created by bernardot on 2/10/16.
 */
public class Virus implements Serializable {
    private Vector2D position;
    private Vector2D velocity;
    private double radius;
    private int SCREEN_RADIUS;

    public Virus() {
        this(0, 0, 0, 0, 1);
    }

    public Virus(double x, double y, double vx, double vy, double rad) {
        position = new Vector2D(x, y);
        velocity = new Vector2D(vx, vy);
        radius = rad;
        SCREEN_RADIUS = Math.max(GameModel.convertWorldLengthToScreenLength(radius), 1);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        SCREEN_RADIUS = Math.max(GameModel.convertWorldLengthToScreenLength(this.radius), 1);
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void draw(Canvas c, Bitmap bmp) {
        int x = GameModel.convertWorldXtoScreenX(position.getX());
        int y = GameModel.convertWorldYtoScreenY(position.getY());

        bmp = Bitmap.createScaledBitmap(bmp, (int) (SCREEN_RADIUS * 2.5), (int) (SCREEN_RADIUS * 2.5), false);
        c.drawBitmap(bmp, x - bmp.getWidth() / 2, y - bmp.getHeight() / 2, null);

        if (GameModel.getPowerUp().getType().equals(Constants.PowerUps.REDUCE_SIZE)) {
            c.drawCircle(x, y, (float) SCREEN_RADIUS, Constants.sickPaintBall);
        } else if (GameModel.getPowerUp().getType().equals(Constants.PowerUps.DOUBLE_POINTS)) {
            c.drawCircle(x, y, (float) SCREEN_RADIUS, Constants.doublePaintBall);
        } else if (GameModel.getPowerUp().getType().equals(Constants.PowerUps.MADNESS)) {
            c.drawCircle(x, y, (float) SCREEN_RADIUS, Constants.madnessPaintBall);
        } else {
            c.drawCircle(x, y, (float) SCREEN_RADIUS, Constants.paintBall);
        }
    }

    public void updatePosition() {
        position.addScaled(velocity, Constants.deltaTime);
    }

    public void increaseRadius(double n) {
        radius += Constants.increaseRadiusFactor * n;
        SCREEN_RADIUS = Math.max(GameModel.convertWorldLengthToScreenLength(radius), 1);
    }

    public void reduceRadius(double n) {
        radius -= Constants.reduceRadiusFactor * n;
        SCREEN_RADIUS = Math.max(GameModel.convertWorldLengthToScreenLength(radius), 1);
    }

    public boolean checkBallInsideLimits(double leftLimit, double rightLimit) {
        if (position.getX() <= leftLimit) {
            velocity.setX(-velocity.getX());
            return true;
        } else if (position.getX() >= rightLimit) {
            velocity.setX(-velocity.getX());
            return true;
        }
        return false;
    }
}
