package liulx.data_center.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Config {
	static String base_url_str = "BASE_URL";
	public static String BASE_URL = "http://172.16.222.52:8080/";
	public static final String URL_LOGIN = "login";
	public static final String INV_IN = "inventory/in";
	public static final String INV_OUT = "inventory/out";
	
	public static final String APP_ID = "liulx.data_center";
	public static final String INV_TYPE = "inv_type";
	
	/**
	 * ªÒ»°Preference…Ë÷√
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static String getBaseURL(Context context){
		return getSharedPreferences(context).getString(base_url_str, "");
	}
	
	public static void setBaseURL(Context context, String baseUrl){
		Editor editor = getSharedPreferences(context).edit();
		editor.putString(base_url_str, baseUrl);
		editor.commit();
		BASE_URL = baseUrl;
		System.out.println("BASE_URL:"+BASE_URL);
	}
	
	public static void initializeConfig(Context context){
		BASE_URL = getBaseURL(context);
	}
}
