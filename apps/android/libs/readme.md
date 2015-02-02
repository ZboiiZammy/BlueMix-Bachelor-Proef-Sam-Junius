Mobile Cloud Services Android SDK for IBM Bluemix
===

This package contains the required native components to interact with the IBM
Bluemix Mobile Cloud Services.  The SDK manages all the communication and security integration between 
the client and with the Mobile Cloud Services in Bluemix.

When you use Bluemix to create a Mobile Cloud Starter application, BlueMix provisions 
multiple services under a single application context. Your mobile application is given 
access to the following mobile services: Mobile Application Security, Push, and Mobile Data.

Version: 1.0.1.20150112-0905

##Installation
The SDK may be installed either by downloading a [zip file](https://mbaas-catalog.ng.bluemix.net/sdk/ibm-baas-sdk-android.zip),
or by installing desired components from [The Central Repository](http://search.maven.org/)
hosted on maven.org. This repository is used by a number of build systems,
but we suggest using [Gradle's repository management functionality](http://www.gradle.org/docs/current/userguide/artifact_dependencies_tutorial.html)
to assist in developing your IBM Bluemix mobile applications.  Using
[Gradle](http://www.gradle.org) can significantly shorten the startup time for new
projects and lessen the burden of managing library version requirements
as well as the dependencies between them.

To install Gradle, please see this link: http://www.gradle.org/docs/current/userguide/installation.html.  If you
are using one of our [samples](https://hub.jazz.net/user/mobilecloud),
a [build.gradle](http://www.gradle.org/docs/current/userguide/tutorial_using_tasks.html)
file has been included for you.

###Contents

The complete SDK consists of a core, plus a collection of components that correspond to function exposed
by the Mobile Cloud Services.  The downloaded zip file
contains all of them. However, each piece of the Android SDK is also available as a separate component
through Gradle and The Central Repository, 
that you can add to your project individually. This allows maximum flexibility, as the developer is able to 
pick and choose the components required for a given application. The Android SDK contains the following 
components, any of which may be added to your project.

The list of components is as follows:
- **[ibmbluemix](https://hub.jazz.net/project/bluemixmobilesdk/ibmbluemix-android/overview)** - This is the foundation of the SDK and controls connection and communication with Backend services
- **[ibmpush](https://hub.jazz.net/project/bluemixmobilesdk/ibmpush-android/overview)** - This is the service SDK for push notification support
- **[ibmdata](https://hub.jazz.net/project/bluemixmobilesdk/ibmdata-android/overview)** - This is the service SDK for cloud data storage
- **[ibmfilesync](https://hub.jazz.net/project/bluemixmobilesdk/ibmfilesync-android/overview)** - This is the service SDK for cloud file storage
- **[ibmcloudcode](https://hub.jazz.net/project/bluemixmobilesdk/ibmcloudcode-android/overview)** - This is the service SDK for cloud code invocation
- **[ibmlocation](https://hub.jazz.net/project/bluemixmobilesdk/ibmlocation-android/overview)** - This is the service SDK for the Beta level mobile location services
- **docs/** - This directory contains the documentation for the SDK

###Supported Android Levels
- Android 2.3.4
- Android 4.0
- Android 4.1
- Android 4.2
- Android 4.3
- Android 4.4

##Getting Started:

Services are associated with a Mobile Cloud application. Connectivity and interaction with
these services depends on the application id, application secret, and application route associated
with a Mobile Cloud Application.

The [Android Development Toolkit (ADT)](http://developer.android.com/sdk/index.html) is a prerequisite.  Once
installed, launch the emulator using the Android Virtual Device
(AVD) manager to run your applications.

IBMBluemix is the entry point for interacting with the Mobile Cloud Services SDKs.  The method initialize
must be invoked before any other API calls.  IBM Bluemix provides information about the current SDK level
and access to service SDKs.

Below is an example of initializing the Mobile Cloud Services SDK.
```java
IBMBluemix.initialize(context, applicationId, applicationSecret, applicationRoute);
IBMCloudCode.initializeService();
IBMData.initializeService();
IBMPush.initializeService();
```
##Learning More
To learn more about using the SDK, please consult the following resources:
- **[Mobile Cloud Services SDK Developer Guide](http://mbaas-gettingstarted.ng.bluemix.net/)**
- **[Samples and Tutorials](https://www.ng.bluemix.net/docs/#starters/mobile/index.html#samples)**
- Visit the **[Bluemix Developers Community](https://developer.ibm.com/bluemix/)**

Connect with Bluemix: [Twitter](https://twitter.com/ibmbluemix) |
[YouTube](https://www.youtube.com/playlist?list=PLzpeuWUENMK2d3L5qCITo2GQEt-7r0oqm) |
[Blog](https://developer.ibm.com/bluemix/blog/) |
[Facebook](https://www.facebook.com/ibmbluemix) |
[Meetup](http://www.meetup.com/bluemix/)

*Licensed Materials - Property of IBM
(C) Copyright IBM Corp. 2013, 2015. All Rights Reserved.
US Government Users Restricted Rights - Use, duplication or
disclosure restricted by GSA ADP Schedule Contract with IBM Corp.*

[Terms of Use](https://hub.jazz.net/project/bluemixmobilesdk/ibmbluemix-android/overview#https://hub.jazz.net/gerrit/plugins/gerritfs/contents/bluemixmobilesdk%252Fibmbluemix-android/refs%252Fheads%252Fmaster/License.txt) |
Notices
