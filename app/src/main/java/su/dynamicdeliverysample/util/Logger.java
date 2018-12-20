package su.dynamicdeliverysample.util;

import android.util.Log;

public final class Logger {

    private final static String LOG_TAG = "DynamicImage";

    private Logger(){
        //private constructor
    }

    public static void logD(String message){
        Log.d(LOG_TAG, message);
    }
}
