package com.example.bernardot.virusattack;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by bernardot on 3/23/16.
 */
public class ButtonWithTouch implements View.OnTouchListener {
    ImageView imageButton;
    boolean blue;

    public ButtonWithTouch(ImageView imageButton, boolean blue) {
        this.imageButton = imageButton;
        this.blue = blue;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!blue)
                    imageButton.setImageResource(R.drawable.button_hover);
                else
                    imageButton.setImageResource(R.drawable.button_blue_hover);
                break;
            case MotionEvent.ACTION_UP:
                if (!blue)
                    imageButton.setImageResource(R.drawable.button);
                else
                    imageButton.setImageResource(R.drawable.button_blue);
                break;
            case MotionEvent.ACTION_OUTSIDE:
                if (!blue)
                    imageButton.setImageResource(R.drawable.button);
                else
                    imageButton.setImageResource(R.drawable.button_blue);
                break;
            default:

                break;
        }
        return false;
    }
}
