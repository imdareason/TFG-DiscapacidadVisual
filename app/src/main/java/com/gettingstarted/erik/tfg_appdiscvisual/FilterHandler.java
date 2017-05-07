package com.gettingstarted.erik.tfg_appdiscvisual;
import org.opencv.core.Scalar;
/**
 * Created by Erik on 07/05/2017.
 */

public class FilterHandler {

    private static FilterHandler filterHandlerInstance;

    private Scalar backgroundColor;
    private Scalar textColor;

    public int backgroundColorOption;
    public int textColorOption;

    private static final int Black = 0;
    private static final int Yellow = 1;
    private static final int Red = 2;
    private static final int Blue = 3;
    private static final int Green = 4;
    private static final int White = 5;

    public static FilterHandler getInstance() {
        if (filterHandlerInstance == null) {
            filterHandlerInstance = new FilterHandler();
        }
        return filterHandlerInstance;
    }

    public void setBackgroundColorOption(int backgroundColorOption) {
        this.backgroundColorOption = backgroundColorOption;
        updateBackgroundColor();
    }

    public void setTextColorOption(int textColorOption) {
        this.textColorOption = textColorOption;
        updateTextColor();
    }

    public Scalar getBackgroundColor() {
        return backgroundColor;
    }

    public Scalar getTextColor() {
        return textColor;
    }

    private void updateBackgroundColor(){
        switch (backgroundColorOption){
            case Black:
                backgroundColor = new Scalar(0,0,0);
                break;
            case Yellow:
                backgroundColor = new Scalar(255,255,0);
                break;
            case Red:
                backgroundColor = new Scalar(255,0,0);
                break;
            case Blue:
                backgroundColor = new Scalar(0,0,255);
                break;
            case Green:
                backgroundColor = new Scalar(0,255,0);
                break;
        }

    }

    private void updateTextColor(){
        switch (textColorOption){
            case Black:
                textColor = new Scalar(0,0,0);
                break;
            default:
                textColor = new Scalar(255,255,255);
                break;
        }
    }
}
