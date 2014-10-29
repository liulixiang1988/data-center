package liulx.data_center.ui;

import java.io.UnsupportedEncodingException;

import liulx.data_center.R;
import liulx.data_center.config.Config;
import liulx.data_center.net.RestClient;
import liulx.util.UIHelper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class DataCenterActivity extends Activity implements OnClickListener {
	private final static int SCANNIN_ITEM_CODE = 1;
	private final static int SCANNIN_INV_CODE = 2;
	
	private static final String TAG = DataCenterActivity.class.getSimpleName();

	private Button btnAddItem, btnSend, btnClear;
	private EditText edtItemCode, edtInvCode;
	private TextView txtItemCodes;
	private String inv_type_url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_center);
		
		switch(getIntent().getIntExtra(Config.INV_TYPE, 0)){
		case 0:
			inv_type_url = Config.INV_IN;
			break;
		case 1:
			inv_type_url = Config.INV_OUT;
			break;
		}
		
		btnAddItem = (Button) findViewById(R.id.btnAddItem);
		btnSend = (Button) findViewById(R.id.btnSend);
		btnClear = (Button) findViewById(R.id.btnClear);
		
		edtItemCode = (EditText) findViewById(R.id.edtItemCode);
		edtInvCode = (EditText) findViewById(R.id.edtInvCode);
		
		txtItemCodes = (TextView) findViewById(R.id.txtItemCodes);
		
		btnAddItem.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		btnClear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//清空
				txtItemCodes.setText("");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnAddItem:
			btnAddItem_onClick();
			break;
		case R.id.btnSend:
			btnSend_onClick();
			break;
		default:
				break;
		}
	}
	
	//添加物料
	private void btnAddItem_onClick(){
		txtItemCodes.append(getItemCode()+";");
		edtItemCode.setText("");
	}
	
	//发送数据
	private void btnSend_onClick(){
		Log.v(TAG, "发送按钮点击");
		if(TextUtils.isEmpty(getItemsCode()) || TextUtils.isEmpty(edtInvCode.getText())){
			UIHelper.ToastMessage(this, "物料条码和货位条码不能为空");
			return;
		}
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("items_code", getItemsCode());
			jsonParams.put("inventory_code", getInvCode());
			Log.v(TAG, jsonParams.toString());
			StringEntity entity = new StringEntity(jsonParams.toString());
			RestClient.postJson(this, inv_type_url, entity, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						String msg = response.getString("msg");
						Log.v(TAG, "返回的消息："+msg);
						UIHelper.ToastMessage(DataCenterActivity.this, msg);
						int resultCode = response.getInt("status");
						if(resultCode == 1){
							clearText();
						}
					} catch (JSONException e) {
						Log.e(TAG, "A发生错误："+e.toString());
						UIHelper.ToastMessage(DataCenterActivity.this, e.toString());
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					Log.e(TAG, "发生错误3 Statuscode:"+statusCode+" ");
					UIHelper.ToastMessage(DataCenterActivity.this, "发生错误3 状态："+statusCode);
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					Log.e(TAG, "发生错误4+status code:"+statusCode);
					for(Header header : headers){
						Log.e(TAG, header.getName()+":"+header.getValue());
					}
					Log.e(TAG, responseString);
					super.onFailure(statusCode, headers, responseString, throwable);
				}
			});
		} 
		catch (JSONException e) {
			Log.e(TAG, "B发生错误："+e.toString());
			UIHelper.ToastMessage(DataCenterActivity.this, e.toString());
		} 
		catch (UnsupportedEncodingException e) {
			Log.e(TAG, "C发生错误："+e.toString());
			UIHelper.ToastMessage(DataCenterActivity.this, e.toString());
		} 
		catch(Exception e){
			Log.e(TAG, "D发生错误" + e.toString());
		}
		Log.v(TAG, "发送结束");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_INV_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				edtInvCode.setText(bundle.getString("result"));
			}
			break;
		case SCANNIN_ITEM_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				edtItemCode.setText(bundle.getString("result"));
			}
			break;
		}
	}
	
	private void clearText(){
		edtItemCode.getText().clear();
		edtItemCode.requestFocus();
		txtItemCodes.setText("");
	}
	
	public String getItemCode(){
		return edtItemCode.getText().toString();
	}
	
	public String getInvCode(){
		return edtInvCode.getText().toString();
	}
	
	public String getItemsCode(){
		return txtItemCodes.getText().toString();
	}
}