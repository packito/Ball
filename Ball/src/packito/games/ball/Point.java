package packito.games.ball;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Point implements GameObject {

	private float x,y;
	public static Bitmap img;
	public static float radius;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void drawOnCanvas(Canvas canvas) {
		canvas.drawBitmap(img, x - radius, y - radius, null);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getRadius() {
		return radius;
	}

	public void go() {
	}

}
