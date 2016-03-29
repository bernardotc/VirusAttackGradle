package com.example.bernardot.virusattack;

import java.io.Serializable;

/**
 * Created by bernardot on 3/22/16.
 */
public class ReduceSize_PowerUp extends PowerUp implements Serializable {
    public ReduceSize_PowerUp() {
        super();
        setDuration(Constants.reduceSizeTime);
        setType(Constants.PowerUps.REDUCE_SIZE);
    }
}
