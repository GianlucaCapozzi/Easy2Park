# Easy2Park

## Description
This is an Android app DEMO project based on Sensoro™ Beacons (https://www.sensoro.com) and Azure Iot Hub (https://azure.microsoft.com/en-us/services/iot-hub/). The idea is to make it easier to find free parking lots. This is done through bluetooth connectivity to Sensoro beacons and Azure cloud computing.     
<br/><br/>
![Schema](https://github.com/GianlucaCapozzi/Easy2Park/blob/master/our_doc/readme_images/drawIO.jpg?raw=true)

## Installation
App has been developed on android studio and tested on Android 9.0, other versions are not supported.
1. Import the Easy2ParkApp folder as a project into Android studio.
2. In Constant.java class, replace SENSORO_DEVICEs Strings with the SN codes of your Sensoro devices.

```java
//Constant.java
package com.example.easy2park;

public class Constant {
    public static final String BLE_STATE_CHANGED_ACTION = "android.bluetooth.adapter.action.STATE_CHANGED";
    public static final String SENSORO_DEVICE1="0117C59B4EC7";
    public static final String SENSORO_DEVICE2="0117C582CAD7";
    public static final String SENSORO_DEVICE3="0117C5578442";
}
```
3. You need to continue with following procedure to register you to Azure.

### First step: register your device on azure iot hub platform

1. Download azure cloud shell, after you need to login to azure from command line:

```
az login
```
2. Register your device on the azure iot hub platform

In order to register your device on the azure iot hub platform, you need to perform the following two commands from
the azure cloud shell:

```
az extension add --name azure-cli-iot-ext
az iot hub device-identity create --hub-name YourIoTHubName --device-id MyAndroidDevice
```
where ```YourIoTHubName``` is the name of the device registered on the azure iot hub platform.

### Second step: obtain the connection string for your device

```
az iot hub device-identity show-connection-string --hub-name YourIoTHubName --device-id MyAndroidDevice --output table
```

### Third step: sent data to azure iot hub platform

1. Put the connection string obtained at step 2 in the file *gradle.properties*
2. Now you can run the app pressing the **START** button
3. In order to check if data are sent to azure iot hub you can check this from the azure cloud shell with the following command:

```
az iot hub monitor-events --hub-name YourIoTHubName --output table
```
### Run your android app

App has been developed on android studio and tested on Android 9.0, other versions are not supported.
1. Make sure your Android device is connected to your computer via a USB cable.
2. Connect the device to WiFi/UMTS network that allows for communication with the server. 
3. Make sure your device is set up for development as explained here (https://developer.android.com/training/basics/firstapp/running-app#RealDevice).
4. Change USB mode to data transfer and allow the RSA fingerprint request of access if asked.
5. Click on run button on Android Studio GUI and select your device.

![Run on device](https://github.com/GianlucaCapozzi/Easy2Park/blob/master/our_doc/readme_images/ss1.png?raw=true)

Alternatively you can use adb from shell (http://delphi.org/2013/11/installing-and-running-android-apps-from-command-line/)


N.B.
You cannot run the app through android studio emulator (at the time of writing) due to the lack of bluetooth connection.  

## Usage 
If you completed the installation part the app will open on your device. <br/>
1. Turn on bluetooth and gps. <br/>
2. Go into the range of your sensoro beacons. <br/>
This will be displayed on your screen: <br/>
<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/mainImg.jpg" height="500" width="500">  
3. Click on Park here to start sending data to Azure <br/>
<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/mainImg2.jpg" height="500" width="500">
4. Click on Leave the parking to stop sending data to Azure <br/>
<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/mainImg3.jpg" height="500" width="500">

## TODO
Features to be implemented:
- Background proper functioning

## Credits:
- Giuseppe Capaldi [<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/gitIcon.png" height="20" width="20" >](https://github.com/not-a-genius)
					[<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/inIcon.png" height="20" width="20" >](https://www.linkedin.com/in/giuseppe-capaldi-56688a171/)

- Gianluca Capozzi [<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/gitIcon.png" height="20" width="20" >](https://github.com/GianlucaCapozzi)
					[<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/inIcon.png" height="20" width="20" >](https://www.linkedin.com/in/gianluca-capozzi-b9a75a16b/)

- Daniele Davoli [<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/gitIcon.png" height="20" width="20" >](https://github.com/danieledavoli)
					[<img src="https://raw.githubusercontent.com/GianlucaCapozzi/Easy2Park/master/our_doc/readme_images/inIcon.png" height="20" width="20" >](https://www.linkedin.com/in/danieledavoli/)
## License:
 Code is under Apache License 2.0.
