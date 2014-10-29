package liulx.data_center.net;

import liulx.data_center.config.Config;

import org.apache.http.HttpEntity;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
public class RestClient {
	
	  public final static String TAG = RestClient.class.getSimpleName();
	  
	  private static AsyncHttpClient client = new AsyncHttpClient();
	  
	  static {
//		  client.setBasicAuth("liulx", "liulx");
		  client.addHeader("Accept", "*/*");
		  client.setUserAgent("data-center-client");
	  }
	  
	  public static AsyncHttpClient getClient(){
		  return client;
	  }

	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void post(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		  Log.v(TAG, "����Url:"+getAbsoluteUrl(url));
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void postJson(Context context, String url, HttpEntity entity,  ResponseHandlerInterface responseHandler){
		  Log.v(TAG, "����Url:"+getAbsoluteUrl(url));
		  String contentType = "application/json";
		  client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return Config.BASE_URL + relativeUrl;
	  }
	  
}
