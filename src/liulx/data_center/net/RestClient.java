package liulx.data_center.net;

import liulx.data_center.AppManager;
import liulx.data_center.config.Config;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.*;
public class RestClient {
	

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
		  System.out.println("«Î«ÛUrl:"+getAbsoluteUrl(url));
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void postJson(Context context, String url, HttpEntity entity,  ResponseHandlerInterface responseHandler){
		  System.out.println("«Î«ÛUrl:"+getAbsoluteUrl(url));
		  String contentType = "application/json";
		  client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return Config.BASE_URL + relativeUrl;
	  }
	  
}
