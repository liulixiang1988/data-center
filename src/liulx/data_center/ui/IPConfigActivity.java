package liulx.data_center.ui;

import liulx.data_center.R;
import liulx.data_center.config.Config;
import liulx.util.UIHelper;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class IPConfigActivity extends Activity implements OnClickListener {

	private Button btnSaveIpConfig;
	private EditText edtIpAddress;
	
	public String getIpAddress(){
		return edtIpAddress.getText().toString();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ipconfig);
		
		btnSaveIpConfig = (Button) findViewById(R.id.btnSaveIpConfig);
		
		edtIpAddress = (EditText) findViewById(R.id.edtIpAddress);
		
		btnSaveIpConfig.setOnClickListener(this); 
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnSaveIpConfig:
			btnSaveIpConfig_click();
		}
	}

	private void btnSaveIpConfig_click(){
		if(TextUtils.isEmpty(edtIpAddress.getText())){
			UIHelper.ToastMessage(this, "IP地址不能为空!");
		}
		
		System.out.println("保存IP为："+getIpAddress());
		Config.setBaseURL(this, getIpAddress());
		UIHelper.ToastMessage(this, "IP保存成功");
		finish();
	}
}
