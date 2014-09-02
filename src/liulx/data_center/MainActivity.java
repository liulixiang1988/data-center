package liulx.data_center;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	private final static int SCANNIN_ITEM_CODE = 1;
	private final static int SCANNIN_INV_CODE = 2;

	private Button btnScanItem, btnScanInv, btnSend;
	private EditText edtItemCode, edtInvCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
}
