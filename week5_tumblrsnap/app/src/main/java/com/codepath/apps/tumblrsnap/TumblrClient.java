package com.codepath.apps.tumblrsnap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TumblrApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TumblrClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TumblrApi.class;
    public static final String REST_URL = "http://api.tumblr.com/v2";
    public static final String REST_CONSUMER_KEY = "BRMEWFUj0PKayqLE6gFLmmfTZ4QWNLhJyezG64R6ja395ePLdY";
    public static final String REST_CONSUMER_SECRET = "UjL8RZmVzlXwxFcQiYlW94wNzos0RPClStTzy24cK23nzwcit8";
    public static final String REST_CALLBACK_URL = "oauth://tumblrsnap";

    public TumblrClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getTaggedPhotos(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("tag", "cptumblrsnap");
        params.put("limit", "20");
        params.put("reblog_info", "1");
        params.put("api_key", REST_CONSUMER_KEY);
        client.get(getApiUrl("tagged"), params, handler);
    }
    
    public void getUserPhotos(AsyncHttpResponseHandler handler) {
    	RequestParams params = new RequestParams();
        params.put("type", "photo");
        params.put("limit", "20");
        params.put("api_key", REST_CONSUMER_KEY);
        client.get(getApiUrl("user/dashboard"), params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        client.get(getApiUrl("user/info"), null, handler);
    }
    
    public void createPhotoPost(String blog, File file, AsyncHttpResponseHandler handler) {
    	RequestParams params = new RequestParams();
    	params.put("type", "photo");
    	params.put("tags", "cptumblrsnap");
    	try {
			params.put("data", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	client.post(getApiUrl(String.format("blog/%s/post?type=photo&tags=cptumblrsnap", blog)), params, handler);
    }
      
    public void createPhotoPost(String blog, Bitmap bitmap, final AsyncHttpResponseHandler handler) {   
        RequestParams params = new RequestParams();
    	params.put("type", "photo");
    	params.put("tags", "cptumblrsnap");
    	
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] bytes = stream.toByteArray();
    	params.put("data", new ByteArrayInputStream(bytes), "image.png");
    	
    	client.post(getApiUrl(String.format("blog/%s/post?type=photo&tags=cptumblrsnap", blog)), params, handler);
    }

    public void createReblogPost(String blog, String idToReblog, String reblogKey, String comment, final AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", idToReblog);
        params.put("comment", comment);
        params.put("reblog_key", reblogKey);
        params.put("tags", "cptumblrsnap");
        Log.i("params", params.toString() + "    url is " + getApiUrl(String.format("blog/%s/post/reblog", blog)));
        client.post(getApiUrl(String.format("blog/%s/post/reblog", blog)), params, handler);
    }

}
