package packito.games.ball;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Playground extends SurfaceView implements Runnable {

	SurfaceHolder holder;
	Thread thread = null;
	GameActivity gameActivity;

	boolean isRunning;
	long timeleft,lastTime;
	long score;
	int width, height;
	boolean soundOn;
	Random rnd;

	SoundPool sPool;
	final int SOUND_WALL;
	final int SOUND_POINT;
	final int SOUND_STAR;

	Ball ball;
	ArrayList<GameObject> objects;

	public Playground(GameActivity gameActivity, DisplayMetrics metrics) {
		super(gameActivity);
		this.gameActivity = gameActivity;
		holder = getHolder();
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		score = 0;
		
		loadImages();

		ball = new Ball(this, width / 2f, height / 2f);
		timeleft = 10000;
		rnd = new Random();
		objects = new ArrayList<GameObject>();
		objects.add(new Point(rnd.nextFloat() * width, rnd.nextFloat() * height));

		sPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		SOUND_WALL = sPool.load(gameActivity, R.raw.wall, 1);
		SOUND_POINT = sPool.load(gameActivity, R.raw.point, 1);
		SOUND_STAR = sPool.load(gameActivity, R.raw.star, 1);
	}

	public void resume() {
		isRunning = true;
		lastTime = System.nanoTime();
		thread = new Thread(this);
		thread.start();
	}

	public void pause() {
		isRunning = false;
	}

	public void run() {
		while (isRunning) {
			if(!holder.getSurface().isValid())
				continue;
			Canvas canvas = holder.lockCanvas();
			canvas.drawColor(Color.WHITE);
			ball.go(canvas);
			ball.drawOnCanvas(canvas);
			ball.handleCollisions();

			for (int o = 0; o < objects.size(); o++) {
				objects.get(o).go();
			}
			for (int o = 0; o < objects.size(); o++) {
				objects.get(o).drawOnCanvas(canvas);
			}

			if(rnd.nextInt(500)==0)
				addStar();
			
			long curTime = System.nanoTime();
			timeleft -= (curTime - lastTime)/1000000;
			lastTime = curTime;
			if(timeleft<=0){
				// TODO GAME OVER
			}
			
			Paint p = new Paint();
			p.setColor(Color.BLACK);
			p.setTextSize(30);
			p.setTypeface(Typeface.DEFAULT);
			String timeString = String.format(":%.1fs", timeleft / 1000.0);
			canvas.drawText(timeString, 0, height, p);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void playSound(int soundID) {
		if (soundOn)
			sPool.play(soundID, 1, 1, 1, 0, 1);
	}

	private void addStar() {
		int random = rnd.nextInt(4);
		switch (random) {
		case 0:
			objects.add(new Star(this, -Star.radius, Star.radius
					+ rnd.nextFloat() * (height - 2 * Star.radius), Star.RIGHT));
			break;
		case 1:
			objects.add(new Star(this, Star.radius + rnd.nextFloat()
					* (width - 2 * Star.radius), -Star.radius, Star.DOWN));
			break;
		case 2:
			objects.add(new Star(this, width + Star.radius, Star.radius
					+ rnd.nextFloat() * (height - 2 * Star.radius), Star.LEFT));
			break;
		case 3:
			objects.add(new Star(this, Star.radius + rnd.nextFloat()
					* (width - 2 * Star.radius), height + Star.radius, Star.UP));
			break;
		}
	}

	private void loadImages() {
		Ball.img = BitmapFactory.decodeResource(gameActivity.getResources(),
				R.drawable.ball);
		Ball.radius = Ball.img.getWidth() / 2f;
		Point.img = BitmapFactory.decodeResource(gameActivity.getResources(),
				R.drawable.point);
		Point.radius = Point.img.getWidth() / 2f;
		Star.img = BitmapFactory.decodeResource(gameActivity.getResources(),
				R.drawable.star);
		Star.radius = Star.img.getWidth() / 2f;
	}
	/*
	public void redraw() {
		
		while (!holder.getSurface().isValid());
		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(Color.WHITE);
		ball.drawOnCanvas(canvas);
		for (int o = 0; o < objects.size(); o++) {
			objects.get(o).drawOnCanvas(canvas);
		}
		holder.unlockCanvasAndPost(canvas);
		
	}
	*/
	
}
