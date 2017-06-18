package com.gettingstarted.erik.tfg_appdiscvisual;

//Android Classes
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

//OpenCV classes

public class MainActivity_show_camera extends AppCompatActivity implements CvCameraViewListener2{
    private static final String TAG = "OCVSample::Activty";
    private static final int backgroundSpinnerOption = 0;

    //Loads camera view of OpenCV for us to use.
    private ZoomCameraView mOpenCvCameraView;

    //SpinnerHandler
    private SpinnerHandler spinnerHandler;

    //FilterActivated
    private boolean filteringIsEnabled;
    private boolean colorDetectionClicked;

    //Spinners and TextView
    Spinner backgroundSpinner;
    Spinner textSpinner;
    TextView backgroundTextView;
    TextView textTextView;

    Mat mRgba;
    Mat mGrayScale;
    Mat mRgbaT;


    public MainActivity_show_camera(){
        spinnerHandler = new SpinnerHandler(backgroundSpinnerOption);
        //TODO: THIS IS HARDCODED! SHOULD BE MARKED IN SETTINGS!
        filteringIsEnabled = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.checkPermissions();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_camera);
        mOpenCvCameraView = (ZoomCameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        backgroundSpinner = (Spinner)findViewById(R.id.spinner);
        backgroundTextView = (TextView)findViewById(R.id.backgroundText);
        if (!filteringIsEnabled){
            //hideFilteringViews();
        }

        backgroundSpinner.setOnItemSelectedListener(spinnerHandler);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setZoomControl((SeekBar) findViewById(R.id.CameraZoomControls));
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void onPause(){
        super.onPause();
        if (mOpenCvCameraView!=null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!OpenCVLoader.initDebug()){
            Log.d(TAG,"Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this,mLoaderCallback);
        }else {
            Log.d(TAG,"OpenCV library found inside package.");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if (mOpenCvCameraView!=null)
            mOpenCvCameraView.disableView();
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }
    public void onCameraViewStarted(int width, int height ){
        mRgba = new Mat(height,width,CvType.CV_8U);
        mGrayScale = new Mat(height,width,CvType.CV_8UC3);
        mRgbaT = new Mat(height,width,CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGrayScale = inputFrame.rgba();

        if (spinnerHandler.filterClicked){
            colorDetectionClicked = false;
            Mat grayScale = inputFrame.gray();
            Mat textColor = new Mat();
            Mat backgroundColor = new Mat();


            boolean isFilterNone = FilterHandler.getInstance().isFilterNone();

            if (!isFilterNone){

                Imgproc.GaussianBlur(grayScale,textColor, new Size(3,3),0);
                Imgproc.adaptiveThreshold(textColor,textColor,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,75,10);
                Core.bitwise_not(textColor,textColor);
                //Text
                //TODO: should inRange be used ?
                mRgba.setTo(FilterHandler.getInstance().getTextColor(),textColor);

                //Background threshold
                Imgproc.threshold(grayScale,backgroundColor,100,255,Imgproc.THRESH_BINARY);
                mRgba.setTo(FilterHandler.getInstance().getBackgroundColor(),backgroundColor);
            }else{return grayScale;}

            grayScale.release();
            backgroundColor.release();
            textColor.release();
        }

        return mRgba;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG,"OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    public void settingsClicked(View view){
        Toast.makeText(getApplicationContext(),R.string.not_implemented_yet,Toast.LENGTH_SHORT).show();
    }


    public void colorDetectionClicked(View view) {

        colorDetectionClicked = true;
        spinnerHandler.filterClicked = false;
        final String[] colorDetected = {""};
        Bitmap bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mGrayScale, bmp);
        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        for (int y = 0; y < bmp.getHeight(); y++) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                int c = bmp.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        Color mainColor = new Color();
        mainColor.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
        String hexColor = String.format("%06X", (0xFFFFFF & Color.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount)));


        RequestManager requestManager = new RequestManager();
        requestManager.setColorToGet(hexColor);
        requestManager.start();
        try {
            requestManager.join();
            Toast.makeText(getApplicationContext(), requestManager.getColorName(), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
