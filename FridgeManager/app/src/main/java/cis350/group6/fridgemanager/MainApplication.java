package cis350.group6.fridgemanager;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Created by Annie on 4/2/2015.
 */

public class MainApplication extends android.app.Application {
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Food.class);
        Parse.initialize(this, "RPkBsLufAmYR4TCMTrcTX4Dgq0iSvaPRTvVMPxLB", "RZKk7RHWH2pNxCrDCp7bg73dOYWCMfOCwIXIDwtY");
    }

}