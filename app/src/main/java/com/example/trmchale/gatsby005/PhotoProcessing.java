package com.example.trmchale.gatsby005;
import android.graphics.BitmapFactory;
import android.os.ResultReceiver;
import android.app.IntentService;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.OpenCVLoader;
import android.os.Bundle;
import android.os.IBinder;

import java.io.ByteArrayOutputStream;

/**
 * Created by trmchale on 6/18/17.
 */

public class PhotoProcessing extends IntentService {
    public final static String BUNDLED_LISTENER = "listener";
    public native void test();
    byte[] bit;
    public PhotoProcessing(){
        super("PhotoProcessing");
    }
    static {System.loadLibrary("native-lib");
        if(!OpenCVLoader.initDebug()){
            Log.d("Login","OpenCV not loaded");
        } else {
            Log.d("Login", "OpenCV works!");
        }}

    @Override
    public void onCreate() {super.onCreate();}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra(PhotoProcessing.BUNDLED_LISTENER);
        byte[] bit = intent.getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bit, 0, bit.length);
        Mat footMat = new Mat();
        Utils.bitmapToMat(bitmap, footMat);
        Mat footGrayMat = new Mat();
        Imgproc.cvtColor(footMat, footGrayMat, Imgproc.COLOR_BGR2GRAY, 1);
        Mat outCannyMat = new Mat();
        Imgproc.Canny(footGrayMat, outCannyMat, 80, 100, 3, false);
        bitmap = OutputGrayMatToFile(outCannyMat, "Canny");
        Bundle bundle = new Bundle();
        // encode
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        bundle.putByteArray("image",byteArray);
        receiver.send(Image.RESULT_OK, bundle);
    }

    private Bitmap OutputGrayMatToFile(Mat mGaryMat, String Filename)
    {
        Mat mRgba = new Mat();
        Imgproc.cvtColor(mGaryMat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);
        Bitmap bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mRgba, bmp);
        return bmp;
    }


}
