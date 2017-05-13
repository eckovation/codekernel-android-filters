package com.filters.utility;

import android.content.Context;
import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

/**
 * Created by akshatgoel on 5/12/17.
 */

public class TransformImage {
    public final static int DEFAULT_BRIGHTNESS = 70;
    public final static int DEFAULT_CONTRAST = 60;
    public final static int DEFAULT_VIGNETTE = 100;
    public final static int DEFAULT_SATURATION = 5;


    public final static int MAX_BRIGHTNESS = 100;
    public final static int MAX_CONTRAST = 100;
    public final static int MAX_VIGENTTE = 255;
    public final static int MAX_SATURATION = 10;

    private String mFilename = null;

    public static final int FILTER_BRIGHTNESS = 0;
    public static final int FILTER_VIGNETTE = 1;
    public static final int FILTER_SATURATION = 2;
    public static final int FILTER_CONTRAST= 3;

    private Bitmap brightnessFilteredBitmap  = null;
    private Bitmap vignetteFilteredBitmap = null;
    private Bitmap saturationFilterBitmap = null;
    private Bitmap contrastFilteredBitmap  = null;

    private Bitmap mBitmap = null;
    private Context mContext;

    public Bitmap getBrightnessFilteredBitmap() {
        return brightnessFilteredBitmap;
    }

    public Bitmap getVignetteFilteredBitmap() {
        return vignetteFilteredBitmap;
    }

    public Bitmap getSaturationFilterBitmap() {
        return saturationFilterBitmap;
    }

    public Bitmap getContrastFilteredBitmap() {
        return contrastFilteredBitmap;
    }

    public String getFilenameForFilter(int filterType) {
        if(filterType == FILTER_BRIGHTNESS) {
            return mFilename+"_brightness.png";
        } else if (filterType == FILTER_VIGNETTE) {
            return mFilename+"_vignette.png";
        } else if (filterType == FILTER_SATURATION) {
            return mFilename+"_saturation.png";
        } else if (filterType == FILTER_CONTRAST) {
            return mFilename+"_contrast.png";
        }

        return mFilename;
    }

    public TransformImage(Context context, Bitmap bitmap) {
        mBitmap = bitmap;
        mContext = context;
        mFilename = System.currentTimeMillis()+"";
    }

    public Bitmap applyBrightnessFilter(int brightness) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new BrightnessSubfilter(brightness));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);

        brightnessFilteredBitmap = outputImage;

        return outputImage;
    }

    public Bitmap applyVignetteSubfilter(Context context, int size) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new VignetteSubfilter(context,size));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);

        vignetteFilteredBitmap = outputImage;

        return outputImage;
    }

    public Bitmap applySaturationSubfilter(int saturation) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new SaturationSubfilter(2 + saturation/10));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);

        saturationFilterBitmap = outputImage;

        return outputImage;
    }

    public Bitmap applyContrastSubfilter(float contrast) {
        Filter myFilter = new Filter();

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,true);

        myFilter.addSubFilter(new ContrastSubfilter(1+(contrast/100)));

        Bitmap outputImage = myFilter.processFilter(mutableBitmap);
        contrastFilteredBitmap = outputImage;

        return outputImage;
    }
}
