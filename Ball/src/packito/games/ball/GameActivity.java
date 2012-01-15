package packito.games.ball;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

public class GameActivity extends Activity implements SensorEventListener,
		OnClickListener {

	Playground playground;
	SensorManager sensorManager;
	WakeLock wl;

	FrameLayout gameMenu;

	static final int PLAYGROUND_ID = 9452343;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		playground = new Playground(GameActivity.this, metrics);
		setContentView(playground);

		gameMenu = (FrameLayout) getLayoutInflater().inflate(
				R.layout.game_menu, null);
		addContentView(gameMenu, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gameMenu.setVisibility(View.GONE);

		Button bResumeGame = (Button) gameMenu.findViewById(R.id.bResumeGame);
		Button bSettings = (Button) gameMenu.findViewById(R.id.bSettings);
		Button bQuitGame = (Button) gameMenu.findViewById(R.id.bQuitGame);
		bQuitGame.setOnClickListener(this);
		bResumeGame.setOnClickListener(this);
		bSettings.setOnClickListener(this);

		playground.setId(PLAYGROUND_ID);
		playground.setOnClickListener(this);
		playground.resume();
	}

	@Override
	protected void onResume() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "wakelock");
		wl.acquire();
		super.onResume();

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		playground.soundOn = getPrefs.getBoolean("sound", true);

		Sensor accSensor = sensorManager.getSensorList(
				Sensor.TYPE_ACCELEROMETER).get(0);
		sensorManager.registerListener(this, accSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// TODO playground je cerny
	}

	@Override
	protected void onPause() {
		wl.release();
		super.onPause();
		sensorManager.unregisterListener(this);
		pauseGame();
	}

	private void pauseGame() {
		playground.pause();
		gameMenu.setVisibility(View.VISIBLE);
	}

	public void onSensorChanged(SensorEvent event) {
		playground.ball.setAcceleration(-event.values[0] / 10,
				event.values[1] / 10);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case PLAYGROUND_ID:
			pauseGame();
			break;
		case R.id.bResumeGame:
			gameMenu.setVisibility(View.GONE);
			playground.resume();
			break;
		case R.id.bSettings:
			Intent i = new Intent("packito.games.ball.SETTINGS");
			startActivity(i);
			break;
		case R.id.bQuitGame:
			finish();
			break;
		}
	}
}