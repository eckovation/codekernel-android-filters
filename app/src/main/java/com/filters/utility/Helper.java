package com.filters.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.filters.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by akshatgoel on 5/12/17.
 */

public class Helper {

    public static Boolean writeDataIntoExternalStorage(Context context, String filename, Bitmap bitmap) {
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getString(R.string.app_name));
        if(!directory.exists() && !directory.mkdirs()) {
            return false;
        }

        File file = new File(directory.getAbsolutePath() + "/" + filename);
        if( file.exists() && !file.canWrite()) {
            return false;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File getFileFromExternalStorage(Context context, String filename) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getString(R.string.app_name) + "/" + filename);

        Log.d(Helper.class.getSimpleName(),"getFileFroExternalStorage " + file.getAbsolutePath());

        if(!file.canRead() || !file.exists()) {
            return null;
        }

        return file;
    }

    public static Boolean writeDataIntoPrivateStorage(Context context,String filename, byte[] bytes) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename,Context.MODE_PRIVATE);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap getBitmapFromPrivateStorage(Context context,String filename) {
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            return BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
