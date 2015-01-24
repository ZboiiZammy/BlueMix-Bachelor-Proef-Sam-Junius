Sample: bluelist-mobiledata-android
===

The bluelist-mobiledata-android sample builds upon the bluelist-base-android sample, a simple grocery list application.  In bluelist-mobiledata-android, the items persist to a mobile backend, using the Mobile Data service.

This sample works with the Mobile Cloud, an application boilerplate that is available on [IBM Bluemix](https://www.ng.bluemix.net).  With this boilerplate, you can quickly incorporate pre-built, managed, and scalable cloud services into your mobile applications without relying on IT involvement. You can focus on building your mobile applications rather than the complexities of managing the back end infrastructure.

This sample uses the Mobile Data service that is a part of the Mobile Cloud boilerplate.  After you run the sample and add some items to the list, you can see your data persisted to the cloud.

Creating the Mobile Cloud boilerplate application
---
1. Login to [IBM Bluemix](https://www.bluemix.net)
2. Click 'Catalog' or 'Create An App'
3. Under Boilerplates, select Mobile Cloud.
4. Enter in App Info & select 'Create'
5. You now have a mobile cloud backend, providing you with some mobile services on the cloud!

Downloading the samples
---
You can clone the samples from IBM DevOps Services with the following command:

    git clone https://hub.jazz.net/git/mobilecloud/bluelist-mobiledata

The bluelist-mobiledata-android sample will be located within the bluelist-mobiledata directory you just created.

Running this sample (bluelist-mobiledata-android)
---

See the instructions in [Build an Android app using the Mobile Data cloud service](http://www.ibm.com/developerworks/library/mo-android-mobiledata-app/index.html) for more information about how to import this sample into your Android development environment and run the sample in a mobile emulator.

Alternatively, follow the instructions below:

Dependency Management in Android Studio
---
First you will need to download the Gradle .zip file from [here](http://www.gradle.org/). To install Gradle, simply extract the downloaded zip into the directory of your choice.

Android Studio will ask for a GRADLE HOME when importing the sample. Set that path to the directory extracted from the Gradle .zip file where the 'bin' directory lives. The 'build.gradle' file will automatically build your project, pulling in the required dependencies.

Otherwise, if using Eclipse etc, on [IBM Bluemix](https://www.bluemix.net) click on your newly created app, then click Download SDKs, and click the Android SDK.
Once downloaded, unzip the SDK, and copy the required jars (ibmbluemix.jar, ibmdata.jar, and ibmfilesync.jar) into the 'libs' folder of your project.

Properties Configuration
---

Last, but not least, navigate to your app's overview page on [IBM Bluemix](https://www.bluemix.net) and copy your AppID, App Secret, and App Route into your 'assets\bluelist.properties' file.

NOTE: Your App Route will be in the format:

```
<YOUR_APP_NAME>.mybluemix.net
```

Run Your App
---

Now you can run your Android Application in your emulator or on your device.

Notice that your data items have persisted. You now have data on the cloud!