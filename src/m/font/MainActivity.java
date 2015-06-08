package m.font;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.media.effect.Effect;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity{
	Paper paper;
	int scrwidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WindowManager wm = getWindowManager();
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		scrwidth = dm.widthPixels;//屏幕宽度
		paper=new Paper(this);
		LayoutParams params=new LayoutParams(scrwidth,scrwidth);
		addContentView(paper,params);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clear(View v){
		paper.clear();
	}

	public void save(View v){
		String sdState = Environment.getExternalStorageState(); // 判断sd卡是否存在
		// 检查SD卡是否可用
		if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "SD卡未准备好！", Toast.LENGTH_SHORT).show();}
		else{
			Toast.makeText(this, "保存成功！\n文件保存在：" + new Bmp(paper.getBitmap()).save(), Toast.LENGTH_LONG).show();		
		}
		paper.clear();
	}

	public class Paper extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cacheBitmap;
		private Path path;

		public Paper(Context context) {
			super(context);
			paint = new Paint();
			paint.setAntiAlias(true); // 抗锯齿
			paint.setPathEffect(new CornerPathEffect(10));
			paint.setStyle(Paint.Style.STROKE); // 画轮廓
			paint.setStrokeCap(Paint.Cap.ROUND);//倒圆角
			paint.setStrokeJoin(Paint.Join.ROUND);//拐角倒圆
			paint.setColor(Color.BLACK); // 颜色
			path = new Path();
			// 创建一张边长为屏幕宽度的位图，作为缓冲

			cacheBitmap = Bitmap.createBitmap(scrwidth,scrwidth, Config.ARGB_8888);
			cacheCanvas = new Canvas(cacheBitmap);
			cacheCanvas.drawColor(Color.WHITE);
		}

		@Override
		protected void onDraw(Canvas canvas) {

			canvas.drawColor(Color.WHITE);  
			// 绘制上一次的，否则不连贯
			canvas.drawBitmap(cacheBitmap, 0, 0, null);
			canvas.drawPath(path, paint);		
		}

		/**
		 * 清空画布
		 */
		public void clear() {
			path.reset();
			cacheCanvas.drawColor(Color.WHITE);
			invalidate();
		}

		/**
		 * 将画布的内容保存到文件
		 * @param filename
		 * @throws FileNotFoundException
		 */
		public Bitmap getBitmap() {
			return Bitmap.createScaledBitmap(cacheBitmap, 256, 256, true);
			/*			return cacheBitmap;*/
		}

		private float cur_x, cur_y;
		private boolean isMoving;
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN : {
				cur_x = x;
				cur_y = y;
				path.moveTo(cur_x, cur_y);
				isMoving = true;
				break;
			}

			case MotionEvent.ACTION_MOVE : {
				if (!isMoving)
					break;
				/*// 二次曲线方式绘制
				path.quadTo(cur_x, cur_y, x, y);*/
				path.lineTo(x, y);
				paint.setStrokeWidth(event.getPressure()*50); // 线条宽度				
				cacheCanvas.drawPath(path, paint);
				cur_x = x;
				cur_y = y;			
				break;
			}

			case MotionEvent.ACTION_UP : {
				path.reset();
				isMoving = false;
				break;
			}
			}

			// 通知刷新界面
			invalidate();

			return true;
		}

	}

}
