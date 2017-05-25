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

    public boolean isFilterNone() {
        return filterNone;
    }

    public void setFilterNone(boolean filterNone) {
        this.filterNone = filterNone;
    }

    private boolean filterNone;

    private static final int GrayScale = 0;
    private static final int BlueText = 1;
    private static final int Black = 2;
    private static final int Yellow = 3;
    private static final int YellowBlueText = 4;
    private static final int Pink = 5;
    private static final int Blue = 6;
    private static final int BlueWhiteText = 7;
    private static final int White = 8;


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
            case GrayScale:
                setFilterNone(true);
                break;
            case Black:
                backgroundColor = new Scalar(0,0,0);
                setTextColorOption(Black);
                setFilterNone(false);
                break;
            case Yellow:
                backgroundColor = new Scalar(255,255,0);
                setTextColorOption(Black);
                setFilterNone(false);
                break;
            case Pink:
                backgroundColor = new Scalar(255,0,255);
                setTextColorOption(Black);
                setFilterNone(false);
                break;
            case Blue:
                backgroundColor = new Scalar(0,255,255);
                setTextColorOption(Black);
                setFilterNone(false);
                break;
            case BlueWhiteText:
                backgroundColor = new Scalar(0,255,255);
                setTextColorOption(White);
                setFilterNone(false);
                break;
            case BlueText:
                backgroundColor = new Scalar(255,255,255);
                setTextColorOption(BlueText);
                setFilterNone(false);

            case YellowBlueText:
                backgroundColor = new Scalar(255,255,0);
                setTextColorOption(BlueText);
                setFilterNone(false);
        }

    }

    private void updateTextColor(){
        switch (textColorOption){
            case Black:
                textColor = new Scalar(0,0,0);
                break;
            case BlueText:
                textColor = new Scalar(0,128,255);
            case White:
                textColor = new Scalar(255,255,255);
                break;
        }
    }
}
