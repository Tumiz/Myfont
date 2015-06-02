package m.font;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View {
	private Paint paint;
	private Canvas cacheCanvas;
	private Bitmap cachebBitmap;
	private Path path;

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint = new Paint();
		paint.setAntiAlias(true); // 抗锯齿
		paint.setStrokeWidth(10); // 线条宽度
		paint.setStyle(Paint.Style.STROKE); // 画轮廓
		paint.setStrokeCap(Paint.Cap.ROUND);//倒圆角
		paint.setStrokeJoin(Paint.Join.ROUND);//拐角倒圆
		paint.setColor(Color.BLACK); // 颜色

		path = new Path();
		// 创建一张屏幕大小的位图，作为缓冲
		cachebBitmap = Bitmap.createBitmap(500,500, Config.ARGB_8888);
		cacheCanvas = new Canvas(cachebBitmap);
		cacheCanvas.drawColor(Color.WHITE);
	}

	public MainView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);  
		// 绘制上一次的，否则不连贯
		canvas.drawBitmap(cachebBitmap, 0, 0, null);
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
		return Bitmap.createScaledBitmap(cachebBitmap, 256, 256, true);
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

			// 二次曲线方式绘制
			path.quadTo(cur_x, cur_y, x, y);
			// 下面这个方法貌似跟上面一样
			// path.lineTo(x, y);
			cur_x = x;
			cur_y = y;
			break;
		}

		case MotionEvent.ACTION_UP : {
			// 鼠标弹起保存最后状态
			cacheCanvas.drawPath(path, paint);
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
