package it.rainbowbreeze.playtog.common;

import android.os.Build;

/**
 * Created by alfredomorresi on 09/01/15.
 */
public class Utils {

    public static boolean runningOnEmulator() {
        return Build.FINGERPRINT.startsWith("generic");
    }
}
