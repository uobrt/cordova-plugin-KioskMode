/*
	Original Work Copyright 2015 (c) Andreas Schrade
	Modified Work Copyright 2015 (c) Behavioral Research and Teaching

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package edu.uoregon.brt.KioskMode;

import org.apache.cordova.CordovaActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KioskActivity extends CordovaActivity {

	// Uncomment to block volume changes
	private final List blockedKeys = new ArrayList(Arrays.asList(
			/*KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP*/
	));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// While the 'window' is active, don't have a lock screen.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		//setContentView(R.layout.activity_main);

		// every time someone enters the kiosk mode, set the flag true
		//PrefUtils.setKioskModeActive(true, getApplicationContext());
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
        //Log.v(TAG, "Window Focus Changed. Is is lock mode?:" + PrefUtils.isKioskModeActive(this.getApplicationContext()));
        boolean isLocked = PrefUtils.isKioskModeActive(this.getApplicationContext());
		if(!hasFocus && isLocked) {
			// Close every kind of system dialog
			Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			sendBroadcast(closeDialog);
		}
	}

	@Override
	public void onBackPressed() {
        boolean isLocked = PrefUtils.isKioskModeActive(this.getApplicationContext());
        if(isLocked) {
            Log.i("Blocked", "Back Button Pressed");
            // nothing to do here
            // … really
        } else {
            // Pass it on back up the chain.
            super.onBackPressed();
        }
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
        boolean isLocked = PrefUtils.isKioskModeActive(this.getApplicationContext());
		if (blockedKeys.contains(event.getKeyCode()) && isLocked) {
			return true;
		} else {
			return super.dispatchKeyEvent(event);
		}
	}
}
