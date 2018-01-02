package io.mob.resu.reandroidsdk;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;

class ShakeDetector implements SensorEventListener {

    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */

    private static final float SHAKE_THRESHOLD_GRAVITY = 20.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private static int mShakeCount;
    int count = 1;
    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            if (mListener != null) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;

                if (mAccel > SHAKE_THRESHOLD_GRAVITY) {
                    Log.e("shake", "" + mAccel);
                    final long now = System.currentTimeMillis();
                    // ignore shake events too close to each other (500ms)
                    if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                        return;
                    }

                    // reset the shake count after 3 seconds of no shakes
                    if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                        mShakeCount = 0;
                    }

                    mShakeTimestamp = now;
                    mShakeCount++;
                    mListener.onShake(mShakeCount);
                }
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    public interface OnShakeListener {

        void onShake(int count);
    }
}
