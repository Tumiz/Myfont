package m.font;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private MainView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		view = (MainView) findViewById(R.id.mainView1);
		findViewById(R.id.iv_btn_save).setOnClickListener(this);
		findViewById(R.id.iv_btn_clear).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mi_exit :
			finish();
			System.exit(0);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_btn_clear :
			view.clear();
			break;
		case R.id.iv_btn_save : {
			String sdState = Environment.getExternalStorageState(); // �ж�sd���Ƿ����
			// ���SD���Ƿ����
			if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
				Toast.makeText(this, "SD��δ׼���ã�", Toast.LENGTH_SHORT).show();
				break;
			}
			Toast.makeText(this, "����ɹ���\n�ļ������ڣ�" + Bmp.save(view.getBitmap()), Toast.LENGTH_LONG).show();
		}
		}
	}
}
