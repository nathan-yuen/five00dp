package com.nyuen.five00dp.api;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.five00dp.structure.CommentResponse;
import com.nyuen.five00dp.structure.PhotoDetailResponse;
import com.nyuen.five00dp.structure.PhotoListResponse;
import com.nyuen.five00dp.structure.PhotoResponse;
import com.nyuen.five00dp.structure.ProfileResponse;
import com.nyuen.five00dp.util.AccountUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();
    private static final String KEY_TOKEN = "auth_token";
    
    public static PhotoListResponse getPhotoStream(String feature, int rpp, int size, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos?feature=" + feature + "&rpp=" + rpp + 
                "&image_size=" + size +
                "&page="+page;
        try {
            PhotoListResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                            PhotoListResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static PhotoListResponse getProfilePhotos(int user_id, int rpp, int size, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos?feature=user&user_id=" + user_id + "&rpp=" + rpp + 
                "&image_size=" + size +
                "&page="+page;
        
        try {
            PhotoListResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                            PhotoListResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    

    public static PhotoDetailResponse getFullPhoto(int photoId){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos/" + photoId + "?comments";
        
        try {
            PhotoDetailResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                    PhotoDetailResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static CommentResponse getComments(int photoId, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos/" + photoId + "/comments?page=" + page;
        try {
            //20 comments per page
            CommentResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                            CommentResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static ProfileResponse getMyProfile(Context context){        
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/users";
        try {
            String json = pxapi.get(url).toString();
            ProfileResponse out = new Gson().fromJson(json, ProfileResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static ProfileResponse getUserProfile(int userID, Context context){        
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/users/show?id=" + userID;
        try {
            String json = pxapi.get(url).toString();
            ProfileResponse out = new Gson().fromJson(json, ProfileResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static PhotoResponse votePhoto(Context context, int photoID, int vote) {
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "photos/" + photoID +"/";
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("id", "" + photoID));
        params.add(new BasicNameValuePair("vote", "" + vote));
//        Log.e(TAG, "" + vote);
//        Log.e(TAG, url);
        try {
            String json = pxapi.post(url, params).toString();
            PhotoResponse out = new Gson().fromJson(json, PhotoResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

}
