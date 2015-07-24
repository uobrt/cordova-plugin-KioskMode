var argscheck = require('cordova/argscheck'),
	utils = require('cordova/utils'),
	exec = require('cordova/exec');

var KioskMode = function() {
};

KioskMode.start = function () {
	cordova.exec(null, null, "KioskMode", "startLockTask", []);
};

KioskMode.stop = function () {
	cordova.exec(null, null, "KioskMode", "stopLockTask", []);
};

module.exports = KioskMode;