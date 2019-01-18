package su.dynamicdeliverysample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DownloadConfirmationDialogActivity extends BaseDynamicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_confirmation_dialog);
        this.setFinishOnTouchOutside(false);
    }
}
