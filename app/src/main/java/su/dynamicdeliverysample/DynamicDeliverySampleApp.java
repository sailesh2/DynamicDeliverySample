package su.dynamicdeliverysample;

import android.content.Context;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.android.play.core.splitcompat.SplitCompatApplication;

public class DynamicDeliverySampleApp extends SplitCompatApplication {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        SplitCompat.install(this);
    }
}
