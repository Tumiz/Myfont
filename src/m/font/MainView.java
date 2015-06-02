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
		paint.setAntiAlias(true); // �����
		paint.setStrokeWidth(10); // �������
		paint.setStyle(Paint.Style.STROKE); // ������
		paint.setStrokeCap(Paint.Cap.ROUND);//��Բ��
		paint.setStrokeJoin(Paint.Join.ROUND);//�սǵ�Բ
		paint.setColor(Color.BLACK); // ��ɫ

		path = new Path();
		// ����һ����Ļ��С��λͼ����Ϊ����
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
		// ������һ�εģ���������
		canvas.drawBitmap(cachebBitmap, 0, 0, null);
		canvas.drawPath(path, paint);		
	}

	/**
	 * ��ջ���
	 */
	public void clear() {
		path.reset();
		cacheCanvas.drawColor(Color.WHITE);
		invalidate();
	}

	/**
	 * �����������ݱ��浽�ļ�
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

			// �������߷�ʽ����
			path.quadTo(cur_x, cur_y, x, y);
			// �����������ò�Ƹ�����һ��
			// path.lineTo(x, y);
			cur_x = x;
			cur_y = y;
			break;
		}

		case MotionEvent.ACTION_UP : {
			// ��굯�𱣴����״̬
			cacheCanvas.drawPath(path, paint);
			path.reset();
			isMoving = false;
			break;
		}
		}

		// ֪ͨˢ�½���
		invalidate();

		return true;
	}

}
