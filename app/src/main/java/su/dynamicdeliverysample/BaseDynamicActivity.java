package su.dynamicdeliverysample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.android.play.core.splitcompat.SplitCompat;

public abstract class BaseDynamicActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        SplitCompat.install(this);
    }
}
