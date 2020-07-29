package com.ece.cov19.Functions;

import android.os.SystemClock;

public class ClickTimeChecker {
    public static long mLastClickTime = 0;

    public static boolean clickTimeChecker() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 200) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    return true;
}
}
