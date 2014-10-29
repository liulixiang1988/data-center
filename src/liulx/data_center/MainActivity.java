package liulx.data_center;


import liulx.data_center.config.Config;
import liulx.data_center.ui.IPConfigActivity;
import liulx.data_center.ui.InvOutActivity;
import liulx.data_center.ui.LoginActivity;
import liulx.util.UIHelper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private Button btnInvIn, btnInvOut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Config.initializeConfig(this);
		
		btnInvIn = (Button) findViewById(R.id.btnInvIn);
		btnInvOut = (Button) findViewById(R.id.btnInvOut);
		
		btnInvIn.setOnClickListener(this);
		btnInvOut.setOnClickListener(this);
		
		if(Config.getBaseURL(this).isEmpty()){
			UIHelper.ToastMessage(this, "IPµÿ÷∑√ª”–≈‰÷√");
			Intent i = new Intent(this, IPConfigActivity.class);
			startActivity(i);
		}
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
		int inv_type = 0;
		Intent i;
		switch(v.getId()){
		case R.id.btnInvIn:
			inv_type = 0;
			i = new Intent(this, LoginActivity.class);
			i.putExtra(Config.INV_TYPE, inv_type);
			startActivity(i);
			finish();
			break;
		case R.id.btnInvOut:
			i = new Intent(this, InvOutActivity.class);
			startActivity(i);
			finish();
			break;
		}

	}
}