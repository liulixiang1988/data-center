package liulx.data_center;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.*;
public class DataCenterRestClient {
	private static final String BASE_URL = "http://172.16.223.183:8080/";

	  private static AsyncHttpClient client = new AsyncHttpClient();
	  
	  static {
		  client.setBasicAuth("liulx", "liulx");
	  }

	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void postJson(Context context, String url, HttpEntity entity,  ResponseHandlerInterface responseHandler){
		  String contentType = "application/json";
		  client.post(context, url, entity, contentType, responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }
	  
}
