package com.nyuen.five00dp.fragment;

import com.nyuen.five00dp.R;
import com.nyuen.five00dp.util.ImageFetcher;
import com.nyuen.five00dp.util.UIUtils;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressLint("ValidFragment")
public class PhotoViewFragment extends DialogFragment {
    
    private ImageView mImage;
    private ImageFetcher mImageFetcher;
    private PhotoViewAttacher mAttacher;
    
    private String mUrl;
    
    public PhotoViewFragment(String url) {
        mUrl = url;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar); // remove title from dialogfragment
        setCancelable(true);
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_photo_view, container, false);
        mImage = (ImageView) view.findViewById(R.id.imageViewDialog);
        
        mImageFetcher.loadImage(mUrl, mImage);
        
        mAttacher = new PhotoViewAttacher(mImage);
        mAttacher.setScaleType(ScaleType.FIT_CENTER);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            
            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();
            }
        });
        
        return view;
    }
}
