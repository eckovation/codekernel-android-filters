package com.filters;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.filters.utility.Helper;
import com.filters.utility.TransformImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

public class ControlActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION = 10;
    private final int PICK_IMAGE = 11;

    private final static String TAG = ControlActivity.class.getSimpleName();

    Toolbar mControlToolbar;
    ImageView mTickImageView;
    ImageView mCenterImageView;

    ImageView firstFilterImageView;
    ImageView secondFilterImageView;
    ImageView thirdFilterImageView;
    ImageView fourthFilterImageView;

    SeekBar filterValueSeekbar;

    Uri mSelectedImageUri;

    int currentFilter;

    private int mScreenWidth;
    private int mScreenHeight;

    TransformImage mTransformImage = null;

    Target mApplySingleFilter = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if(currentFilter == TransformImage.FILTER_BRIGHTNESS) {
                mTransformImage.applyBrightnessFilter(filterValueSeekbar.getProgress());

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_BRIGHTNESS),mTransformImage.getBrightnessFilteredBitmap());
                File targetFile = Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_BRIGHTNESS));

                Picasso.with(ControlActivity.this).invalidate(targetFile);
                Picasso.with(ControlActivity.this).load(targetFile).resize(0,mScreenHeight/2).into(mCenterImageView);
            } else if(currentFilter == TransformImage.FILTER_CONTRAST) {
                mTransformImage.applyContrastSubfilter(filterValueSeekbar.getProgress());

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_CONTRAST),mTransformImage.getContrastFilteredBitmap());
                File targetFile = Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_CONTRAST));

                Picasso.with(ControlActivity.this).invalidate(targetFile);
                Picasso.with(ControlActivity.this).load(targetFile).resize(0,mScreenHeight/2).into(mCenterImageView);
            } else if(currentFilter == TransformImage.FILTER_VIGNETTE) {
                mTransformImage.applyVignetteSubfilter(ControlActivity.this,filterValueSeekbar.getProgress());

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_VIGNETTE),mTransformImage.getVignetteFilteredBitmap());
                File targetFile = Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_VIGNETTE));

                Picasso.with(ControlActivity.this).invalidate(targetFile);
                Picasso.with(ControlActivity.this).load(targetFile).resize(0,mScreenHeight/2).into(mCenterImageView);
            } else if(currentFilter == TransformImage.FILTER_SATURATION) {
                mTransformImage.applySaturationSubfilter(filterValueSeekbar.getProgress());

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_SATURATION),mTransformImage.getSaturationFilterBitmap());
                File targetFile = Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_SATURATION));

                Picasso.with(ControlActivity.this).invalidate(targetFile);
                Picasso.with(ControlActivity.this).load(targetFile).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(TAG,"Error in loading bitmap");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d(TAG,"Loading bitmap");
        }
    };

    Target mSmallTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d(TAG,"Loading bitmap");

            mTransformImage = new TransformImage(ControlActivity.this, bitmap);

            mTransformImage.applyBrightnessFilter(TransformImage.DEFAULT_BRIGHTNESS);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_BRIGHTNESS),mTransformImage.getBrightnessFilteredBitmap());
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_BRIGHTNESS))).fit().centerInside().into(firstFilterImageView);

            Log.d(TAG,"Applied brightness bitmap");

            mTransformImage.applySaturationSubfilter(TransformImage.DEFAULT_SATURATION);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_SATURATION),mTransformImage.getSaturationFilterBitmap());
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_SATURATION))).fit().centerInside().into(secondFilterImageView);

            Log.d(TAG,"Applied saturation bitmap");

            mTransformImage.applyContrastSubfilter(TransformImage.DEFAULT_CONTRAST);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_CONTRAST),mTransformImage.getContrastFilteredBitmap());
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_CONTRAST))).fit().centerInside().into(thirdFilterImageView);

            Log.d(TAG,"Applied contrast bitmap");

            mTransformImage.applyVignetteSubfilter(ControlActivity.this,TransformImage.DEFAULT_VIGNETTE);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_VIGNETTE),mTransformImage.getVignetteFilteredBitmap());
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_VIGNETTE))).fit().centerInside().into(fourthFilterImageView);

            Log.d(TAG,"Applied vignette");
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(TAG,"Loading bitmap failed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d(TAG,"Loading onPrepareLoad");
        }
    };

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        firstFilterImageView    = (ImageView) findViewById(R.id.firstFilter);
        secondFilterImageView   = (ImageView) findViewById(R.id.secondFilter);
        thirdFilterImageView    = (ImageView) findViewById(R.id.thirdFilter);
        fourthFilterImageView   = (ImageView) findViewById(R.id.fourthFilter);
        filterValueSeekbar      = (SeekBar) findViewById(R.id.seekBar);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        mControlToolbar = (Toolbar) findViewById(R.id.toolbar);

        mControlToolbar.setTitle(getString(R.string.app_name));
        mControlToolbar.setNavigationIcon(R.drawable.icon);
        mControlToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        mTickImageView = (ImageView) findViewById(R.id.imageView3);

        mTickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(ControlActivity.this).load(mSelectedImageUri).into(mApplySingleFilter);
            }
        });

        mCenterImageView = (ImageView) findViewById(R.id.centerImageView);
        mCenterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mCenterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermissions();

                if(ContextCompat.checkSelfPermission(ControlActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        firstFilterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterValueSeekbar.setMax(TransformImage.MAX_BRIGHTNESS);
                filterValueSeekbar.setProgress(TransformImage.DEFAULT_BRIGHTNESS);

                currentFilter = TransformImage.FILTER_BRIGHTNESS;
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_BRIGHTNESS))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        secondFilterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterValueSeekbar.setMax(TransformImage.MAX_SATURATION);
                filterValueSeekbar.setProgress(TransformImage.DEFAULT_SATURATION);

                currentFilter = TransformImage.FILTER_SATURATION;
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_SATURATION))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        thirdFilterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterValueSeekbar.setMax(TransformImage.MAX_CONTRAST);
                filterValueSeekbar.setProgress(TransformImage.DEFAULT_CONTRAST);

                currentFilter = TransformImage.FILTER_CONTRAST;
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_CONTRAST))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        fourthFilterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterValueSeekbar.setMax(TransformImage.MAX_VIGENTTE);
                filterValueSeekbar.setProgress(TransformImage.DEFAULT_VIGNETTE);

                currentFilter = TransformImage.FILTER_VIGNETTE;
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilenameForFilter(TransformImage.FILTER_VIGNETTE))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data == null) {return;}
            Uri selectedImageUri = data.getData();
            if(selectedImageUri == null) {return;}

            mSelectedImageUri = selectedImageUri;

            Picasso.with(ControlActivity.this).load(selectedImageUri).fit().centerInside().into(mCenterImageView);
            Picasso.with(ControlActivity.this).load(selectedImageUri).into(mSmallTarget);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Permission Granted!");

                    new MaterialDialog.Builder(this)
                            .title(R.string.permission_title)
                            .content(R.string.permission_granted_content)
                            .positiveText(R.string.permission_agree)
                            .show();

                } else {
                    Log.d(TAG,"Permission Denied!");
                }
        }
    }

    public void requestStoragePermissions() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ControlActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(ControlActivity.this)
                                .title(R.string.permission_title)
                                .content(R.string.permission_content)
                                .positiveText(R.string.permission_agree_settings)
                                .negativeText(R.string.permission_cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                    }
                                })
                                .canceledOnTouchOutside(true).show();
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION);
            }
        }
    }

}
