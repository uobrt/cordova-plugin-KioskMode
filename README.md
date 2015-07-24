# KioskMode

A Cordova plugin that implements a toggleable kiosk mode on Android devices without access to android's screen pinning api.

Essentially a series of dirty hacks as described here:

http://www.andreas-schrade.de/2015/02/16/android-tutorial-how-to-create-a-kiosk-mode-in-android/

## Installation

Navigate to your project root and run:

```sh
cordova plugin add https://github.com/uobrt/cordova-plugin-KioskMode.git
```

## Usage

### start

```js
window.plugins.KioskMode.start();
```

### stop

```js
window.plugins.KioskMode.stop();
```

## Full Functionality

In order to use the full functionality of this plugin, a slight modification to the generated java source files
must be made. 

0. Write down the widget id in your cordova project config.xml file. 

1. Open your android MainActivity.java file. Your Widget ID becomes a file path, so that edu.brt.uoregon.KioskMode
becomes edu/brt/uoregon/KioskMode.

```
platforms/android/src/***[Your/Widget/Id/Path]***/MainActivity.java
```

2. At the end of the import statements, add the following line:

```java
import edu.uoregon.brt.KioskMode.KioskActivity;
```

3. In the class declaration replace:

```java
public class MainActivity extends CordovaActivity
```

with 

```java
public class MainActivity extends KioskActivity
```

From here, ```cordova build android``` tends to fail with the message 
```Error: No Java files found which extend CordovaActivity.```

```KioskActivity``` actually extends ```CordovaActivity``` but Cordova doesn't see it. Until a better workaround is found
we still need to override a few things from CordovaActivity in order to handle system dialogs and back presses. 
Import ```platforms/Android``` into Android Studio and build the project manually. All will be well.

## Notes

### Back Button
While in Kiosk Mode, the back button can't exit your app, but you may want to disable it within your app 
for good measure. Make sure to do this after ```DeviceReady``` has been fired.
```js
document.addEventListener("backbutton", function (e) {
	e.preventDefault();
}, false );
```

### Other Plugins to Use

- ```mesmotronic/cordova-fullscreen-plugin``` The immersive mode complements KioskMode nicely.

- ```livesure/cordova-plugin-locktask``` A less hacky solution but only works on Android 5.0+