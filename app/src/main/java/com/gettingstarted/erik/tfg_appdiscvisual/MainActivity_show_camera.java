package com.gettingstarted.erik.tfg_appdiscvisual;

//Android Classes
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

//OpenCV classes
import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import org.opencv.android.OpenCVLoader;

public class MainActivity_show_camera extends AppCompatActivity implements CvCameraViewListener2{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_camera);
        mOpenCvCameraView = (JavaCameraView)findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
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
        mRgba = new Mat(height,width,CvType.CV_8UC4);
        mRgbaF = new Mat(height,width,CvType.CV_8UC4);
        mRgbaT = new Mat(height,width,CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        //Bitmap bitmapImage = null;
        //Utils.matToBitmap(mRgba,bitmapImage);
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        /*Core.transpose(mRgba,mRgbaT);
        Imgproc.resize(mRgbaT,mRgbaF,mRgbaF.size(),0,0,0);
        Core.flip(mRgbaF,mRgba,1);
*/
        return mRgba;
    }

    private static final String TAG = "OCVSample::Activty";

    //Loads camera view of OpenCV for us to use.
    private CameraBridgeViewBase mOpenCvCameraView;

    //Used in Camera selection from menu
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;

    public MainActivity_show_camera(){
        Log.i(TAG,"Instantiated new "+this.getClass());
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

    
}
