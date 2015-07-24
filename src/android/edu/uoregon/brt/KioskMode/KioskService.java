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

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.uoregon.brt.KinderBasics.MainActivity;

public class KioskService extends Service {

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1); // periodic interval to check in seconds -> 1 second
    private static final String TAG = KioskService.class.getSimpleName();

    //private Activity kioskActivity = null;
    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;

    // Return the instance of the running KioskService
    /*public static KioskService getInstance() {
        return sInstance;
    }*/

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        running =false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'KioskService'");
        running = true;
        ctx = this;

        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    Log.i(TAG, "Thread Running");
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread interrupted: 'KioskService'");
                    }
                }while(running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }

    private void handleKioskMode() {
        // is Kiosk Mode active?
        if(PrefUtils.isKioskModeActive(ctx)) {
            // is App in background?
            if(isInBackground()) {
                restoreApp(); // restore!
            }
        }
    }

    private boolean isInBackground() {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return (!ctx.getApplicationContext().getPackageName().equals(componentInfo.getPackageName()));
    }

    private void restoreApp() {
        // Restart activity
        //if(kioskActivity != null) {
            Intent i = new Intent(ctx, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        /*} else {
            Log.i(TAG, "Tried to restore activity that did not exist");
        }*/
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}