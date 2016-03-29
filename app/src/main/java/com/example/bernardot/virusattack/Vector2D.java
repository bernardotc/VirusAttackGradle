package com.example.bernardot.virusattack;

import java.io.Serializable;

/**
 * Created by bernardot on 2/10/16.
 */
public class Vector2D implements Serializable {
    private double x;
    private double y;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double magnitude() {
        return Math.hypot(this.x, this.y);
    }

    public void normalise() {
        double mag = magnitude();
        this.x /= mag;
        this.y /= mag;
    }

    public void multiplyScalar(double num) {
        this.x *= num;
        this.y *= num;
    }

    public double scalarProduct(Vector2D v) {
        return this.x * v.x + this.y * v.y;
    }

    public void addScaled(Vector2D v, double fraction) {
        this.x += v.x * fraction;
        this.y += v.y * fraction;
    }

    public Vector2D rotateVector90degreesAnticlockwise() {
        return new Vector2D(-this.y, this.x);
    }

    public static Vector2D minus(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }
}
