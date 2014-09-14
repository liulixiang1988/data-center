package liulx.data_center.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import liulx.data_center.R;
import liulx.data_center.R.id;
import liulx.data_center.R.layout;
import liulx.data_center.config.Config;
import liulx.data_center.net.RestClient;
import liulx.util.UIHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	
	private Button btnLogin;
	private EditText edtUserName, edtPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		edtUserName = (EditText) findViewById(R.id.edtUserName);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		
		btnLogin.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (TextUtils.isEmpty(edtUserName.getText())|| TextUtils.isEmpty(edtPassword.getText())){
			UIHelper.ToastMessage(this, "用户名和密码都不能为空");
			return;
		}
		
		RequestParams params = new RequestParams();
		params.put("username", edtUserName.getText().toString());
		params.put("password", edtPassword.getText().toString());

		RestClient.post(Config.URL_LOGIN, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String token = response.getString("token");
					System.out.println("返回的令牌："+token);
					RestClient.getClient().addHeader("Authorization", "Token "+token);
					UIHelper.ToastMessage(LoginActivity.this, "登录成功");
					Intent i = new Intent(LoginActivity.this, DataCenterActivity.class);
					int inv_type = LoginActivity.this.getIntent().getIntExtra(Config.INV_TYPE, 0);
					i.putExtra(Config.INV_TYPE, inv_type);
					startActivity(i);
					finish();
				} catch (JSONException e) {
					System.out.println("A发生错误："+e.toString());
					UIHelper.ToastMessage(LoginActivity.this, e.toString());
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				System.out.println("发生错误3"+throwable.getMessage());
				for(Header header : headers){
					System.out.println(header.getName()+":"+header.getValue());
				}
				UIHelper.ToastMessage(LoginActivity.this, throwable.getMessage());
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				System.out.println("发生错误4+status code:"+statusCode);
				for(Header header : headers){
					System.out.println(header.getName()+":"+header.getValue());
				}
				System.out.println(responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
		
		
	}

}
