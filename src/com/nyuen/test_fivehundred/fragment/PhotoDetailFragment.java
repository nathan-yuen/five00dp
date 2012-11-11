package com.nyuen.test_fivehundred.fragment;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.adapter.PhotoDetailAdapter;
import com.nyuen.test_fivehundred.api.ApiHelper;
import com.nyuen.test_fivehundred.structure.CommentResponse;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.ImageFetcher;
import com.nyuen.test_fivehundred.util.UIUtils;

@SuppressLint("NewApi")
public class PhotoDetailFragment extends ListFragment implements AbsListView.OnScrollListener {
    
    private static final String TAG = PhotoDetailFragment.class.getSimpleName();
    
    public static final String INTENT_EXTRA_PHOTO = TAG + ".INTENT_EXTRA_PHOTO";

    private PhotoDetailAdapter mPhotoDetailAdapter;
    private ImageFetcher mImageFetcher;
    
    private boolean mLoading = false;
    private View mHeaderView;
    private View mLoadingView;
    private Photo mPhoto;
    private int mPage = 1;
    private int mTotalPage = 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.photo_header, null);
        mLoadingView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_footer, null);
        mPhoto = (Photo) getArguments().getParcelable(INTENT_EXTRA_PHOTO);
        
        getActivity().getActionBar().setTitle(mPhoto.name);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_detail_fragment, container, false);   
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addFooterView(mLoadingView);
        getListView().addHeaderView(mHeaderView, null, false);
       
        updateHeaderView();
        
        new LoadPhotoCommentsTask().execute();
    }
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount + 2 >= totalItemCount;
        
        if(loadMore && !mLoading && (mPage <= mTotalPage)) {
            new LoadPhotoCommentsTask().execute();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            mImageFetcher.setPauseWork(true);
        } else {
            mImageFetcher.setPauseWork(false);
        }
    }
    
    private void updateHeaderView() {
        ImageView headerPhotoView = (ImageView) mHeaderView.findViewById(R.id.headerPhotoView);
        ImageView headerUserPhotoView = (ImageView) mHeaderView.findViewById(R.id.headerUserPhotoView);
        TextView headerUserNameView = (TextView) mHeaderView.findViewById(R.id.headerUserNameView);
        TextView headerDescriptionView = (TextView) mHeaderView.findViewById(R.id.headerDescriptionView);
        TextView viewsCountView = (TextView) mHeaderView.findViewById(R.id.viewsCountView);
        TextView votesCountView = (TextView) mHeaderView.findViewById(R.id.votesCountView);
        TextView favsCountView = (TextView) mHeaderView.findViewById(R.id.favsCountView);
        TextView ratingView = (TextView) mHeaderView.findViewById(R.id.ratingView);
                
        mImageFetcher.loadImage(mPhoto.image_url, headerPhotoView);
        mImageFetcher.loadImage(mPhoto.user.userpic_url, headerUserPhotoView);
        headerUserNameView.setText(mPhoto.user.fullname);
        viewsCountView.setText("" + mPhoto.times_viewed + " Views");
        votesCountView.setText("" + mPhoto.votes_count + " Votes");
        favsCountView.setText("" + mPhoto.favorites_count + " Favorites");
        headerDescriptionView.setText("Using the shutter speed to create a painterly effect with the moving water");//Html.fromHtml(mPhoto.description));       
        ratingView.setText("" + mPhoto.rating);
    }
    
    private void updateList(CommentResponse response) {
        if(mPage == 1) {
            mPhotoDetailAdapter = new PhotoDetailAdapter(getActivity(), mImageFetcher);
            //TODO
            //mPhotoDetailAdapter.setDetails(response);
            mPhotoDetailAdapter.setComments(Arrays.asList(response.comments));
            mTotalPage = response.total_pages;
            setListAdapter(mPhotoDetailAdapter);    
            getListView().setOnScrollListener(this);
        } else {   
            mPhotoDetailAdapter.appendComments(Arrays.asList(response.comments));
            mPhotoDetailAdapter.notifyDataSetChanged();
        }
    }
    
    private class LoadPhotoCommentsTask extends AsyncTask<Void, Void, CommentResponse> {
        protected void onPreExecute() {
            mLoading = true;
            mLoadingView.setVisibility(View.VISIBLE);
        }

        protected CommentResponse doInBackground(Void... params) {
            return ApiHelper.getComments(mPhoto.id, mPage);
        }

        protected void onPostExecute(CommentResponse response) {
            mLoadingView.setVisibility(View.GONE);
            if(response != null) {
                updateList(response);
                mPage++;
                mLoading = false;
            }
        }
    }

}
