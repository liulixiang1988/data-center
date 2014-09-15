package liulx.data_center.ui;

import java.io.UnsupportedEncodingException;

import liulx.data_center.R;
import liulx.data_center.R.id;
import liulx.data_center.R.layout;
import liulx.data_center.R.menu;
import liulx.data_center.config.Config;
import liulx.data_center.net.RestClient;
import liulx.util.UIHelper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataCenterActivity extends Activity implements OnClickListener {
	private final static int SCANNIN_ITEM_CODE = 1;
	private final static int SCANNIN_INV_CODE = 2;

	private Button btnScanItem, btnScanInv, btnSend;
	private EditText edtItemCode, edtInvCode;
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
		
		btnScanItem = (Button) findViewById(R.id.btnScanItem);
		btnScanInv = (Button) findViewById(R.id.btnScanInv);
		btnSend = (Button) findViewById(R.id.btnSend);
		
		edtItemCode = (EditText) findViewById(R.id.edtItemCode);
		edtInvCode = (EditText) findViewById(R.id.edtInvCode);
		
		
		btnScanItem.setOnClickListener(this);
		btnScanInv.setOnClickListener(this);
		btnSend.setOnClickListener(this);
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
		case R.id.btnScanInv:
			btnScanInv_onClick();
			break;
		case R.id.btnScanItem:
			btnScanItem_onClick();
			break;
		case R.id.btnSend:
			btnSend_onClick();
			break;
		default:
				break;
		}
	}
	
	//扫描货位
	private void btnScanInv_onClick(){
		Intent intent = new Intent();
		intent.setClass(this, MipcaActivityCapture.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, SCANNIN_INV_CODE);
	}
	
	//扫描物料
	private void btnScanItem_onClick(){
		Intent intent = new Intent();
		intent.setClass(this, MipcaActivityCapture.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, SCANNIN_ITEM_CODE);
	}
	
	//发送数据
	private void btnSend_onClick(){
		System.out.println("发送按钮点击");
		if(TextUtils.isEmpty(edtItemCode.getText()) || TextUtils.isEmpty(edtInvCode.getText())){
			UIHelper.ToastMessage(this, "物料条码和货位条码不能为空");
			return;
		}
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("item_code", edtItemCode.getText().toString());
			jsonParams.put("inventory_code", edtInvCode.getText().toString());
			System.out.println(jsonParams.toString());
			StringEntity entity = new StringEntity(jsonParams.toString());
			RestClient.postJson(this, inv_type_url, entity, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						String msg = response.getString("msg");
						System.out.println("返回的消息："+msg);
						UIHelper.ToastMessage(DataCenterActivity.this, msg);
						int resultCode = response.getInt("status");
						if(resultCode == 1){
							clearText();
						}
					} catch (JSONException e) {
						System.out.println("A发生错误："+e.toString());
						UIHelper.ToastMessage(DataCenterActivity.this, e.toString());
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					System.out.println("发生错误3 Statuscode:"+statusCode+" ");
					UIHelper.ToastMessage(DataCenterActivity.this, "发生错误3 状态："+statusCode);
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
		catch (JSONException e) {
			System.out.println("B发生错误："+e.toString());
			UIHelper.ToastMessage(DataCenterActivity.this, e.toString());
		} 
		catch (UnsupportedEncodingException e) {
			System.out.println("C发生错误："+e.toString());
			UIHelper.ToastMessage(DataCenterActivity.this, e.toString());
		} 
		catch(Exception e){
			System.out.println("D发生错误" + e.toString());
		}
		System.out.println("发送结束");
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
		edtInvCode.getText().clear();
		edtItemCode.requestFocus();
	}
}