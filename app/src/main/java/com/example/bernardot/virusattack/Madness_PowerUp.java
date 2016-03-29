package com.example.bernardot.virusattack;

import java.io.Serializable;

/**
 * Created by bernardot on 3/23/16.
 */
public class Madness_PowerUp extends PowerUp implements Serializable {
    public Madness_PowerUp() {
        super();
        setDuration(Constants.madnessTime);
        setType(Constants.PowerUps.MADNESS);
    }
}
