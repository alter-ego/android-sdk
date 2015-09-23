package com.sensorberg.sdk.demo;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.sensorberg.sdk.Logger;
import com.sensorberg.sdk.action.InAppAction;
import com.sensorberg.sdk.action.UriMessageAction;
import com.sensorberg.sdk.action.VisitWebsiteAction;
import com.sensorberg.sdk.bootstrapper.BackgroundDetector;
import com.sensorberg.sdk.bootstrapper.SensorbergApplicationBootstrapper;
import com.sensorberg.sdk.demo.HistoryService.HistoryService;
import com.sensorberg.sdk.demo.demoOne.BuildConfig;
import com.sensorberg.sdk.resolver.BeaconEvent;

@SuppressWarnings("javadoc")
public class DemoApplication extends Application
{
    private static final String TAG = "DemoApplication";
    private SensorbergApplicationBootstrapper boot;
    private BackgroundDetector detector;

    //show all internal logging in debug mode
    static {
        if (BuildConfig.DEBUG) {
            Logger.enableVerboseLogging();
        }
    }

    private Context activityContext;

    @Override
	public void onCreate() {
		super.onCreate();
        Log.d(TAG, "onCreate application");

        HistoryService.restoreList(this);

        boot = new SensorbergApplicationBootstrapper(this, true){
            @Override
            public void presentBeaconEvent(BeaconEvent beaconEvent) {
                String what = beaconEvent.trigger == 1 ? " ,enter" : " ,exit";
                switch (beaconEvent.getAction().getType()) {
                    case MESSAGE_URI:
                        UriMessageAction uriMessageAction = (UriMessageAction) beaconEvent.getAction();
                        showAlert(uriMessageAction.getUuid().hashCode(), uriMessageAction.getTitle(), uriMessageAction.getContent() + what, Uri.parse(uriMessageAction.getUri()));
                        break;
                    case MESSAGE_WEBSITE:
                        VisitWebsiteAction visitWebsiteAction = (VisitWebsiteAction) beaconEvent.getAction();
                        showAlert(visitWebsiteAction.getUuid().hashCode(), visitWebsiteAction.getSubject(), visitWebsiteAction.getBody() + what, visitWebsiteAction.getUri());
                        break;
                    case MESSAGE_IN_APP:
                        InAppAction inAppAction = (InAppAction) beaconEvent.getAction();
                        showAlert(inAppAction.getUuid().hashCode(), inAppAction.getSubject(), inAppAction.getBody() + what, inAppAction.getUri());
                        break;
                }

                HistoryService.addAction(beaconEvent.getAction(), getApplicationContext());
            }
        };
        boot.activateService("8961ee72ea4834053b376ad54007ea277cba4305db12188b74d104351ca8bf8a");
//        boot.activateService("f403d31bbb86faf31793b0fadb2a43f2ce0402968eaa92769da1048dba0ee2f0");  //Billa
        boot.hostApplicationInForeground();

        detector = new BackgroundDetector(boot);
        registerActivityLifecycleCallbacks(detector);



	}

    public void showAlert(int hashCode, String subject, String body, final Uri uri) {
        if (activityContext == null){
            MyActionPresenter.showNotification(getApplicationContext(), hashCode, subject, body, uri);
            return;
        }
        new AlertDialog.Builder(activityContext)
                .setTitle(subject)
                .setMessage(body)
                .setPositiveButton("open url", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"open URL " + uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    public void setActivityContext(Context activityContext) {
        this.activityContext = activityContext;
    }

    public Context getActivityContext() {
        return activityContext;
    }
}
