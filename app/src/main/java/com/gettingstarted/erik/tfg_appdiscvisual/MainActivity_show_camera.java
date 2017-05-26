package com.gettingstarted.erik.tfg_appdiscvisual;

//Android Classes
import android.os.Bundle;
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
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_camera);
        mOpenCvCameraView = (ZoomCameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        backgroundSpinner = (Spinner)findViewById(R.id.spinner);
        backgroundTextView = (TextView)findViewById(R.id.backgroundText);
        if (!filteringIsEnabled){
            hideFilteringViews();
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

    public void onCameraViewStarted(int width, int height ){
        mRgba = new Mat(height,width,CvType.CV_8U);
        mGrayScale = new Mat(height,width,CvType.CV_8U);
        mRgbaT = new Mat(height,width,CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        if (filteringIsEnabled){
            Mat grayScale = inputFrame.gray();
            Mat textColor = new Mat();
            Mat backgroundColor = new Mat();

            //Text threshold
            //TODO: should inRange be used ?
            boolean isFilterNone = FilterHandler.getInstance().isFilterNone();

            if (!isFilterNone){
                Imgproc.threshold(grayScale,textColor,128,255,Imgproc.THRESH_OTSU);
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


    public void colorDetectionClicked(View view){

        Mat HSV = new Mat();
        Mat threshold = new Mat();

        Imgproc.cvtColor(mRgba,HSV,Imgproc.COLOR_RGB2HSV);

        Core.inRange(HSV,new Scalar(0,60,60), new Scalar(0,100,100),threshold);
        Toast.makeText(getApplicationContext(),R.string.not_implemented_yet,Toast.LENGTH_SHORT).show();
    }

    private void hideFilteringViews(){
        backgroundSpinner.setVisibility(Spinner.GONE);
        textSpinner.setVisibility(Spinner.GONE);
        backgroundTextView.setVisibility(TextView.GONE);
        textTextView.setVisibility(TextView.GONE);
    }
}
