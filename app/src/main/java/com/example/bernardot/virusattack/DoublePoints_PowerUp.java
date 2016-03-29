package com.example.bernardot.virusattack;

import java.io.Serializable;

/**
 * Created by bernardot on 3/22/16.
 */
public class DoublePoints_PowerUp extends PowerUp implements Serializable {
    public DoublePoints_PowerUp() {
        super();
        setDuration(Constants.doublePointsTime);
        setType(Constants.PowerUps.DOUBLE_POINTS);
    }
}
