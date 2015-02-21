/*
 * Copyright 2014 IBM Corp. All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.bluelist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.ibm.bluelist.dataobjects.BestPractise;
import com.ibm.bluelist.dataobjects.Topic;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.push.IBMPush;
import com.ibm.mobile.services.push.IBMPushNotificationListener;
import com.ibm.mobile.services.push.IBMSimplePushNotification;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import bolts.Continuation;
import bolts.Task;

public final class BlueListApplication extends Application {
    public static final int EDIT_ACTIVITY_RC = 1;
    public static IBMPush push = null;

    private Activity mActivity;

    private static final String deviceAlias = "TargetDevice";
    private static final String consumerID = "MBaaSListApp";
    private static final String APP_ID = "applicationID";
    private static final String APP_SECRET = "applicationSecret";
    private static final String APP_ROUTE = "applicationRoute";
    private static final String PROPS_FILE = "bluelist.properties";
    private static final String CLASS_NAME = BlueListApplication.class.getSimpleName();

    private IBMPushNotificationListener notificationListener = null;

    List<Topic> topicList;
    List<BestPractise> bestPractiseList;

    public BlueListApplication() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(CLASS_NAME, "Activity created: " + activity.getLocalClassName());
                mActivity = activity;

                notificationListener = new IBMPushNotificationListener() {
                    @Override
                    public void onReceive(final IBMSimplePushNotification message) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Class<? extends Activity> actClass = mActivity.getClass();
                                if(actClass == TopicActivity.class){
                                    ((TopicActivity)mActivity).listBPs();
                                    Log.e(CLASS_NAME, "Notification message received: " + message.toString());
                                    if(!message.getAlert().contains("BPList was updated")){
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new AlertDialog.Builder(mActivity).setTitle("Push notification received").setMessage(message.getAlert()).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                };
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;
                Log.d(CLASS_NAME, "Activity started: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mActivity = activity;
                Log.d(CLASS_NAME, "Activity resumed: " + activity.getLocalClassName());
                if (push != null){
                    push.listen(notificationListener);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(CLASS_NAME, "Activity saved instance state: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if(push != null){
                    push.hold();
                }
                Log.d(CLASS_NAME, "Activity paused: " + activity.getLocalClassName());
                if(activity != null && activity.equals(mActivity))
                    mActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(CLASS_NAME, "Activity stopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(CLASS_NAME, "Activity destroyed: " + activity.getLocalClassName());
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        topicList = new ArrayList<Topic>();
        bestPractiseList = new ArrayList<BestPractise>();
        // Read from properties file.
        Properties props = new java.util.Properties();
        Context context = getApplicationContext();
        try {
            AssetManager assetManager = context.getAssets();
            props.load(assetManager.open(PROPS_FILE));
            Log.i(CLASS_NAME, "Found configuration file: " + PROPS_FILE);
        } catch (FileNotFoundException e) {
            Log.e(CLASS_NAME, "The bluelist.properties file was not found.", e);
        } catch (IOException e) {
            Log.e(CLASS_NAME,
                    "The bluelist.properties file could not be read properly.", e);
        }
        Log.i(CLASS_NAME, "Application ID is: " + props.getProperty(APP_ID));

        // Initialize the IBM core backend-as-a-service.
        IBMBluemix.initialize(this, props.getProperty(APP_ID), props.getProperty(APP_SECRET), props.getProperty(APP_ROUTE));
        // Initialize the IBM Data Service.
        IBMData.initializeService();
        // Register the Topic Specialization.
        Topic.registerSpecialization(Topic.class);
        BestPractise.registerSpecialization(BestPractise.class);
        // Initialize IBM Push Service.
        IBMPush.initializeService();
        // Retrieve instance of the IBM Push service.
        push = IBMPush.getService();
        // Register the device with the IBM Push service.
        push.register(deviceAlias, consumerID).continueWith(new Continuation<String, Void>() {
            @Override
            public Void then(Task<String> task) throws Exception {
                if (task.isCancelled()) {
                    Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                } else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                } else {
                    Log.d(CLASS_NAME, "Device Successfully Registered");
                }

                return null;
            }
        });
    }

    /**
     * returns the topicList, an array of Topic objects.
     *
     * @return topicList
     */
    public List<Topic> getTopicList() {
        return topicList;
    }

    public List<BestPractise> getBestPractiseList() {
        return bestPractiseList;
    }
}