package packito.games.ball;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Star implements GameObject {

	private float x, y;
	public static Bitmap img;
	public static float radius;

	private Playground playground;
	private int direction;

	public static final int RIGHT = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int UP = 3;

	public Star(Playground playground, float x, float y, int direction) {
		this.playground = playground;
		this.x = x;
		this.y = y;
		this.direction = direction;
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
		switch (direction) {
		case RIGHT:
			x += 3;
			if (x > playground.width + radius)
				remove();
			break;
		case DOWN:
			y += 3;
			if (y > playground.height + radius)
				remove();
			break;
		case LEFT:
			x -= 3;
			if (x < -radius)
				remove();
			break;
		case UP:
			y -= 3;
			if (y < -radius)
				remove();
			break;
		}
	}

	public void remove() {
		for (int i = 0; i < playground.objects.size(); i++) {
			if (playground.objects.get(i).equals(this)) {
				playground.objects.remove(i);
				break;
			}
		}
	}

}
