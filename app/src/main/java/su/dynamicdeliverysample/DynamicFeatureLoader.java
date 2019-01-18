package su.dynamicdeliverysample;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.google.android.play.core.splitinstall.SplitInstallHelper;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.util.Set;

import su.dynamicdeliverysample.callback.IProgressCallback;
import su.dynamicdeliverysample.util.Logger;

class DynamicFeatureLoader {
    private final static String PACKAGE_NAME = "su.dynamicdeliverysample";
    private final static String CLASS_NAME = "su.dynamicimage.DynamicImageActivity";

    private Context mContext;
    private SplitInstallManager mSplitInstallManager;
    private SplitInstallRequest mSplitRequest;
    private String mDynamicFeature;
    private int mCurrentInstallSession;
    private OnSuccessListener<Integer> mSuccessListener;
    private OnCompleteListener<Integer> mCompleteListener;
    private OnFailureListener mFailureListener;
    private SplitInstallStateUpdatedListener mStateUpdateListener;
    private IProgressCallback mProgressCallback;

    DynamicFeatureLoader(Context context, String dynamicFeature){
        mContext = context;
        mDynamicFeature = dynamicFeature;

        setupListeners();
        setupSplitInstaller();
        registerStateListener();
    }

    void setProgressCallback(IProgressCallback progressCallback){
        mProgressCallback = progressCallback;
    }

    void load(){
        mProgressCallback.onStart();

        if(alreadyInstalled()){
            openDynamicImageModule();
        }else {
            installDynamicImageModule();
        }
    }

    private boolean alreadyInstalled(){
        Set<String> installedModules = mSplitInstallManager.getInstalledModules();
        boolean isInstalled = false;
        if(installedModules != null) {
            isInstalled = installedModules.contains(mDynamicFeature);
        }
        Logger.logD("Is already Installed - " + String.valueOf(isInstalled));
        return isInstalled;
    }

    private void installDynamicImageModule(){

        mSplitInstallManager.startInstall(mSplitRequest)
                .addOnSuccessListener(mSuccessListener)
                .addOnFailureListener(mFailureListener);


    }

    private void setupListeners(){

        mSuccessListener = new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer session) {
                Logger.logD("onsuccess + session - " + session.toString());
                mCurrentInstallSession = session;
            }
        };

        mCompleteListener = new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(Task<Integer> task) {
                if(task.isComplete() && task.isSuccessful() && task.getResult() == mCurrentInstallSession){
                    Logger.logD("Loaded");
                    openDynamicImageModule();
                }else{
                    Logger.logD("Failed");
                }
            }
        };

        mFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Logger.logD("Failed with " + e.getMessage());
                destroy();
            }
        };

        mStateUpdateListener = new SplitInstallStateUpdatedListener(){

            @Override
            public void onStateUpdate(SplitInstallSessionState state) {
                Logger.logD("state update - "+state.toString());
                if (state.status() == SplitInstallSessionStatus.FAILED
                        && state.errorCode() == SplitInstallErrorCode.SERVICE_DIED) {
                    // Retry the request.
                    Logger.logD("SERVICE DIED");
                    destroy();
                    return;
                }
                if (state.sessionId() == mCurrentInstallSession) {
                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            Logger.logD("Downloading");
                            break;

                        case SplitInstallSessionStatus.INSTALLED:
                            Logger.logD("Installed");
                            openDynamicImageModule();
                            break;
                        case SplitInstallSessionStatus.FAILED:
                            Logger.logD("Failed");
                            destroy();
                            break;
                        case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                            askUserPermission(state.resolutionIntent().getIntentSender());
                            break;

                    }
                }
            }
        };
    }

    private void openDynamicImageModule(){
        mProgressCallback.onComplete();

        if(Build.VERSION.SDK_INT > 25 && Build.VERSION.SDK_INT < 28) {
            SplitInstallHelper.updateAppInfo(mContext);
            startDynamicImageActivity(mContext);

            Logger.logD("app context update");
        }else {


            Context newContext = null;
            try {
                newContext = mContext.createPackageContext(mContext.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (newContext != null) {
                Logger.logD("app context new");
                startDynamicImageActivity(newContext);
            }
            else{
                Logger.logD("app context old");
                startDynamicImageActivity(mContext);
            }

        }
    }

    private void startDynamicImageActivity(final Context context){
        final Intent intent = new Intent();
        intent.setClassName(PACKAGE_NAME,CLASS_NAME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                context.startActivity(intent);
            }
        });
    }

    private void registerStateListener(){
        if(mSplitInstallManager != null) {
            mSplitInstallManager.registerListener(mStateUpdateListener);
        }
    }

    private void unRegisterStateListener(){
        if(mSplitInstallManager != null) {
            mSplitInstallManager.unregisterListener(mStateUpdateListener);
        }
    }


    private void setupSplitInstaller(){
        mSplitInstallManager = SplitInstallManagerFactory.create(mContext);
        mSplitRequest = SplitInstallRequest.newBuilder()
                                            .addModule(mDynamicFeature)
                                            .build();
    }

    private void askUserPermission(IntentSender intentSender){
        try {
            mContext.startIntentSender(intentSender, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Logger.logD("Could not start ask user intent" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void destroy(){
        unRegisterStateListener();
        mProgressCallback.onComplete();
    }
}

