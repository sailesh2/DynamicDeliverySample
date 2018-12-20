package su.dynamicdeliverysample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import su.dynamicdeliverysample.callback.IProgressCallback;


public class MainActivity extends AppCompatActivity {

    private final static String DYNAMIC_IMAGE_FEATURE = "dynamicimage";
    private Button mOpenDynamicImageButton;
    private DynamicFeatureLoader mDynamicFeatureLoader;
    private AlertDialog.Builder mDynamicFeatureDownloadAlert;
    private View.OnClickListener mOpenDynamicImageClickListener;
    private DialogInterface.OnClickListener mPositiveAlertClickListener;
    private DialogInterface.OnClickListener mNegativeAlertClickListener;
    private Toast mDismissToast;
    private Toast mDownloadingToast;
    private ProgressBar mSpinner;
    private IProgressCallback mProgressCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupListener();
        init();
    }

    private void setupListener(){
        mOpenDynamicImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadAlert();
            }
        };

        mPositiveAlertClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadDynamicModule();
            }
        };

        mNegativeAlertClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDismissToast.show();
            }
        };

        mProgressCallback = new IProgressCallback() {
            @Override
            public void onStart() {
                mSpinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete() {
                mSpinner.setVisibility(View.GONE);
            }
        };
    }

    private void init(){
        mDismissToast = Toast.makeText(this, "Sorry, it can't be opened without download", Toast.LENGTH_SHORT);
        mDownloadingToast = Toast.makeText(this, "Its downloading, and will be opened soon", Toast.LENGTH_SHORT);
        mSpinner = findViewById(R.id.downloadSpinnerId);

        setupDownloadAlert();
        setupDynamicFeatureLoader();
    }

    private void setupDynamicFeatureLoader(){
        mDynamicFeatureLoader =  new DynamicFeatureLoader(this, DYNAMIC_IMAGE_FEATURE);
        mDynamicFeatureLoader.setProgressCallback(mProgressCallback);
        mOpenDynamicImageButton = (Button) findViewById(R.id.openDynamicModuleId);
        mOpenDynamicImageButton.setOnClickListener(mOpenDynamicImageClickListener);
    }

    private void setupDownloadAlert(){
        mDynamicFeatureDownloadAlert = new AlertDialog.Builder(this);
        mDynamicFeatureDownloadAlert.setTitle("Download dynamic module might increase app size, please allow");
        mDynamicFeatureDownloadAlert.setPositiveButton("OK", mPositiveAlertClickListener);
        mDynamicFeatureDownloadAlert.setNegativeButton("CANCEL", mNegativeAlertClickListener);
    }

    private void showDownloadAlert(){
        mDynamicFeatureDownloadAlert.show();
    }

    private void downloadDynamicModule(){
        mDynamicFeatureLoader.load();
        mDownloadingToast.show();
    }
}
