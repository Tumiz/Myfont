package m.font;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity{
	private MainView mv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mv=(MainView) findViewById(R.id.mainView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void clear(View v){
		mv.clear();
	}
	
	public void save(View v){
		String sdState = Environment.getExternalStorageState(); // 判断sd卡是否存在
		// 检查SD卡是否可用
		if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "SD卡未准备好！", Toast.LENGTH_SHORT).show();}
		else{
			Toast.makeText(this, "保存成功！\n文件保存在：" + Bmp.save(mv.getBitmap()), Toast.LENGTH_LONG).show();		
		}
		mv.clear();
	}

}
