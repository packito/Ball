package packito.games.ball;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.FloatMath;

public class Ball {

	private float x, y, dx, dy, ax, ay;
	public static Bitmap img;
	public static float radius;
	Playground playground;

	public static final float FRICTION = 0.98f;
	public static final float ELASTICITY = 0.8f;

	public Ball(Playground playground, float x, float y) {
		this.x = x;
		this.y = y;
		this.playground = playground;
		dx = dy = ax = ay = 0;
		radius = img.getWidth() / 2f;
	}

	public void handleCollisions() {
		ArrayList<GameObject> objects = playground.objects;
		for (int i = 0; i < objects.size(); i++) {
			GameObject o = objects.get(i);
			if (FloatMath.sqrt((x - o.getX()) * (x - o.getX()) + (y - o.getY())
					* (y - o.getY())) < radius + o.getRadius()) {
				objects.remove(i);
				i--;
				if (o instanceof Point) {
					playground.playSound(playground.SOUND_POINT);
					playground.timeleft+=1000;
					Random r = new Random();
					objects.add(new Point(Point.radius + r.nextFloat()
							* (playground.width - 2 * Point.radius),
							Point.radius + r.nextFloat()
									* (playground.height - 2 * Point.radius)));
				} else if (o instanceof Star) {
					playground.playSound(playground.SOUND_STAR);
					playground.timeleft += 5000;
				}
			}
		}
	}

	public void setAcceleration(float ax, float ay) {
		this.ax = ax;
		this.ay = ay;
	}

	public void go(Canvas canvas) {
		dx += ax;
		dy += ay;
		dx *= FRICTION;
		dy *= FRICTION;
		x += dx;
		y += dy;
		if (x < radius) {
			if (dx < -1)
				playground.playSound(playground.SOUND_WALL);
			x = radius;
			dx = -dx * ELASTICITY;
		} else if (x > canvas.getWidth() - radius) {
			if (dx > 1)
				playground.playSound(playground.SOUND_WALL);
			x = canvas.getWidth() - radius;
			dx = -dx * ELASTICITY;
		}
		if (y < radius) {
			if (dy < -1)
			playground.playSound(playground.SOUND_WALL);
			y = radius;
			dy = -dy * ELASTICITY;
		} else if (y > canvas.getHeight() - radius) {
			if (dy > 1)
			playground.playSound(playground.SOUND_WALL);
			y = canvas.getHeight() - radius;
			dy = -dy * ELASTICITY;
		}
	}

	public void drawOnCanvas(Canvas canvas) {
		canvas.drawBitmap(img, x - radius, y - radius, null);
	}
}
