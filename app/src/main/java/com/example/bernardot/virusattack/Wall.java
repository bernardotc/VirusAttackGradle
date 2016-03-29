package com.example.bernardot.virusattack;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.Serializable;

/**
 * Created by bernardot on 2/10/16.
 */
public class Wall implements Serializable {
    private Vector2D startPosition, endPosition, tangentVector, normalVector;
    private double length;
    private Double depth;

    public Wall(double sx, double sy, double ex, double ey) {
        // Setting depth to null. This means barrier is infinite.
        this(sx, sy, ex, ey, null);
    }

    public Wall(double sx, double sy, double ex, double ey, Double d) {
        startPosition = new Vector2D(sx, sy);
        endPosition = new Vector2D(ex, ey);
        depth = d;

        // Calculate wall length
        Vector2D lengthVector = Vector2D.minus(endPosition, startPosition);
        length = lengthVector.magnitude();

        // Obtain tangent vector
        tangentVector = lengthVector;
        tangentVector.normalise();

        // Obtain normal vector;
        normalVector = tangentVector.rotateVector90degreesAnticlockwise();
    }

    public Vector2D getStartPosition() {
        return startPosition;
    }

    public Vector2D getEndPosition() {
        return endPosition;
    }

    public boolean isBallCollidingWall(Vector2D ballCentre, double ballRadius) {
        Vector2D vectorFromStartPosToCircleCentre = Vector2D.minus(ballCentre, startPosition);
        double circleDistanceFromWall = vectorFromStartPosToCircleCentre.scalarProduct(normalVector);
        double circleDistanceAlongWall = vectorFromStartPosToCircleCentre.scalarProduct(tangentVector);

        return circleDistanceAlongWall >= 0 && circleDistanceFromWall <= ballRadius && (depth == null || circleDistanceFromWall >= -(depth + ballRadius))
                && circleDistanceAlongWall >= 0 && circleDistanceAlongWall <= length;
    }

    public Vector2D calculateVelocityAfterACollision(Vector2D velocity) {
        double vParallel = velocity.scalarProduct(tangentVector);
        double vNormal = velocity.scalarProduct(normalVector);
        if (vNormal < 0) // Assumes normal points AWAY from wall...
            vNormal = -vNormal * Constants.coefficientOfRestitution;
        Vector2D result = new Vector2D(tangentVector);
        result.multiplyScalar(vParallel);
        result.addScaled(normalVector, vNormal);
        return result;
    }

    public void draw(Canvas c, Bitmap membrane) {
        int x1 = GameModel.convertWorldXtoScreenX(startPosition.getX());
        int y1 = GameModel.convertWorldYtoScreenY(startPosition.getY());
        int x2 = GameModel.convertWorldXtoScreenX(endPosition.getX());
        int y2 = GameModel.convertWorldYtoScreenY(endPosition.getY());

        int timesImage = (int) Math.ceil(GameModel.getScreenHeightStatic() / (double) membrane.getHeight());
        if (x1 < GameModel.getScreenWidthStatic() / 2) {
            for (int x = 0; x < timesImage; x++) {
                c.drawBitmap(membrane, x1 - membrane.getWidth(), membrane.getHeight() * x, null);
            }
        } else {
            for (int x = 0; x < timesImage; x++) {
                c.drawBitmap(membrane, x1, membrane.getHeight() * x, null);
            }
        }
        c.drawLine(x1, y1, x2, y2, Constants.paintWall);
    }
}
