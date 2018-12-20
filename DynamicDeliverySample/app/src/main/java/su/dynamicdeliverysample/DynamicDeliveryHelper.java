package su.dynamicdeliverysample;

import android.content.Context;

import com.google.android.play.core.splitinstall.SplitInstallHelper;

public final class DynamicDeliveryHelper {

    public static void loadNativeLib(Context context, String lib){
        SplitInstallHelper.loadLibrary(context, lib);
    }
}
