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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class OnScreenOffReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
            Context ctx = context.getApplicationContext();
            // is Kiosk Mode active?
            if(PrefUtils.isKioskModeActive(ctx)) {
                wakeUpDevice();
            }
        }
    }

    private void wakeUpDevice() {
        KioskMode plugin = KioskMode.getInstance();
        PowerManager.WakeLock wakeLock = plugin.getWakeLock();
        if (wakeLock.isHeld()) {
            wakeLock.release(); // release old wake lock
        }

        // create a new wake lock...
        wakeLock.acquire();

        // ... and release again
        wakeLock.release();
    }


}