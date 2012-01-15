package packito.games.ball;

import android.graphics.Canvas;

public interface GameObject {
	
	public float getX();
	
	public float getY();
	
	public float getRadius();
	
	public void drawOnCanvas(Canvas canvas);
	
	public void go();
}
