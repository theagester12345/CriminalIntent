package com.example.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class pictureUtils  {

    public static Bitmap getScale (String path, int destWidth, int destHeight){

        //Read dimensions of saved image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        //Get width and height of saved image
        float srcWidth =options.outWidth;
        float srcHeight = options.outHeight;

        //find scale
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Read in and return new bitmap
        return  BitmapFactory.decodeFile(path,options );

    }


    public static Bitmap getScale(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScale(path,size.x,size.y);

    }


}
