    package com.gettingstarted.erik.tfg_appdiscvisual;


    import android.graphics.Bitmap;
    import android.graphics.Color;

    /**
     * Created by Erik on 29/05/2017.
     */

    public class ColorDetector {

        public String getHexColor(Bitmap bmp){
            float redBucket = 0;
            float greenBucket = 0;
            float blueBucket = 0;
            float pixelCount = 0;

            int redValue,greenValue,blueValue;


            for (int y = 0; y < bmp.getHeight(); y++) {
                for (int x = 0; x < bmp.getWidth(); x++) {
                    int c = bmp.getPixel(x, y);

                    pixelCount++;
                    redBucket += Color.red(c);
                    greenBucket += Color.green(c);
                    blueBucket += Color.blue(c);
                }
            }

            redValue = Math.round(redBucket / pixelCount);
            greenValue = Math.round(greenBucket / pixelCount);
            blueValue = Math.round(blueBucket / pixelCount);
            final String hexColor = String.format("%06X", (0xFFFFFF & Color.rgb(redValue,
                    greenValue,
                    blueValue)));

            return hexColor;
        }

    }
