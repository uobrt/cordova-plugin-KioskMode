package edu.uoregon.brt.KioskMode;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

public class KioskMode extends CordovaPlugin {

	private static final String ACTION_START_LOCK_TASK = "startLockTask";
	private static final String ACTION_STOP_LOCK_TASK = "stopLockTask";

	private Activity activity = null;
    private Context ctx = null;
    private Application app = null;

    private PowerManager.WakeLock wakeLock;
    private OnScreenOffReceiver onScreenOffReceiver;

    private static KioskMode instance;

    public static KioskMode getInstance() {
        return instance;
    }

    @Override public void pluginInitialize() {
        instance = this;
        activity = cordova.getActivity();
        ctx = activity.getApplicationContext();
        app = activity.getApplication();

        PrefUtils.setKioskModeActive(false, ctx);

        registerKioskModeScreenOffReceiver();
        startKioskService();

    }

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		try {
			if (ACTION_START_LOCK_TASK.equals(action)) {

				if (!PrefUtils.isKioskModeActive(ctx)) {
                    PrefUtils.setKioskModeActive(true, ctx);
				}

				callbackContext.success();

				return true;

			} else if (ACTION_STOP_LOCK_TASK.equals(action)) {

				if (PrefUtils.isKioskModeActive(ctx)) {
                    PrefUtils.setKioskModeActive(false, ctx);
				}

				callbackContext.success();
				return true;

			} else {

				callbackContext.error("The method '" + action + "' does not exist.");
				return false;

			}
		} catch (Exception e) {

			callbackContext.error(e.getMessage());
			return false;

		}
	}

    private void registerKioskModeScreenOffReceiver() {
        // register screen off receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        onScreenOffReceiver = new OnScreenOffReceiver();
        app.registerReceiver(onScreenOffReceiver, filter);
    }

    public PowerManager.WakeLock getWakeLock() {
        if(wakeLock == null) {
            // lazy loading: first call, create wakeLock via PowerManager.
            PowerManager pm = (PowerManager) app.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
        }
        return wakeLock;
    }

    private void startKioskService() { // ... and this method
        app.startService(new Intent(app, KioskService.class));
    }
}
