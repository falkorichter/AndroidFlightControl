package de.falkorichter.android.flightcontrol;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.EditText;

public class AndroidFlightControlActivity extends Activity implements SensorEventListener {
	
	private SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private Sensor mAccelerometer;
    private Sensor mGravitySensor;
    private Sensor mLinerarAccelerationSensor;
    private Sensor mRotationVectorSensor;
    private Display mDisplay;
    private WakeLock mWakeLock;
    
    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;
    private long mCpuTimeStamp;
    
    private EditText mSensorXText;
    private EditText mSensorYText;
    private EditText mSensorZText;
	
    private EditText mSensorRotationXText;
    private EditText mSensorRotationYText;
	
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mSensorXText = (EditText)findViewById(R.id.editText_X);
        mSensorYText = (EditText)findViewById(R.id.editText_Y);
        mSensorZText = (EditText)findViewById(R.id.editText_Z);
        mSensorRotationXText = (EditText)findViewById(R.id.editTextRotation_X);
        mSensorRotationYText = (EditText)findViewById(R.id.editTextRotation_Y);
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass()
                .getName());
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mLinerarAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
        mWakeLock.acquire();
        startSensorListener();
    }
    
 
    @Override
    protected void onPause() {
        super.onPause();
        stopSensorListener();
        
        // and release our wake-lock
        mWakeLock.release();
    }
    
    private void startSensorListener(){
    	mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    	mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    private void stopSensorListener() {
        mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//We don«t care about the Accuracy (yet)
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			switch (mDisplay.getRotation()) {
			case Surface.ROTATION_0:
				mSensorX = event.values[0];
				mSensorY = event.values[1];
				mSensorZ = event.values[2];
				break;
			case Surface.ROTATION_90:
				mSensorX = -event.values[1];
				mSensorY = event.values[0];
				mSensorZ = event.values[2];
				break;
			case Surface.ROTATION_180:
				mSensorX = -event.values[0];
				mSensorY = -event.values[1];
				mSensorZ = event.values[2];
				break;
			case Surface.ROTATION_270:
				mSensorX = event.values[1];
				mSensorY = -event.values[0];
				mSensorZ = event.values[2];
				break;
			}

			mSensorXText.setText(Float.toString(mSensorX));
			mSensorYText.setText(Float.toString(mSensorY));
			mSensorZText.setText(Float.toString(mSensorZ));

			mSensorTimeStamp = event.timestamp;
			mCpuTimeStamp = System.nanoTime();
		}
		else if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
			mSensorRotationXText.setText(Float.toString(event.values[0]));
			mSensorRotationYText.setText(Float.toString(event.values[1]));
		}
	}

}