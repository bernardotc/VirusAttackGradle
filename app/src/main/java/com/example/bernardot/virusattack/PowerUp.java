package com.example.bernardot.virusattack;

import java.io.Serializable;

/**
 * Created by bernardot on 3/22/16.
 */
public class PowerUp implements Serializable {
    private int duration;
    private Constants.PowerUps type;

    public PowerUp() {
        this.duration = 50;
        this.type = Constants.PowerUps.NONE;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Constants.PowerUps getType() {
        return type;
    }

    public void setType(Constants.PowerUps type) {
        this.type = type;
    }
}
