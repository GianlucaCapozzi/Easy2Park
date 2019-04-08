# Easy2Park

## Description:
This is an Android app DEMO project based on Sensoro™ Beacons (https://www.sensoro.com) and Azure Iot Hub (https://azure.microsoft.com/en-us/services/iot-hub/). The idea is to make it easier to find free parking lots. This is done through bluetooth connectivity to Sensoro beacons and Azure cloud computing.     

## Installation:
App has been developed on android studio and tested on Android 9.0, other versions are not supported.
1. Make sure your Android device is connected to your computer via a USB cable.
2. Connect the device to WiFi/UMTS network that allows for communication with the server. 
3. Make sure your device is set up for development as explained here (https://developer.android.com/training/basics/firstapp/running-app#RealDevice).
4. Change USB mode to data transfer and allow the RSA fingerprint request of access if asked.
5. Click on run button on Android Studio GUI and select your device.

![Run on device](https://github.com/not-a-genius/Easy2Park/edit/master/our_doc/readme_images/ss1.png)

Of course you can also use adb as:
```
adb shell am start -n com.package.name/com.package.name.ActivityName
```


N.B.
You cannot run the app thorugh android studio emulator (at the time of writing) due to the lack of bluetooth connection.  

## Usage: 
The next section is usage, in which you instruct other people on how to use your project after they’ve installed it. This would also be a good place to include screenshots of your project in action.

## Contributing:
 Larger projects often have sections on contributing to their project, in which contribution instructions are outlined. Sometimes, this is a separate file. If you have specific contribution preferences, explain them so that other developers know how to best contribute to your work. To learn more about how to help others contribute, check out the guide for setting guidelines for repository contributors.

## Credits:
 Include a section for credits in order to highlight and link to the authors of your project.

## License:
 Finally, include a section for the license of your project. For more information on choosing a license, check out GitHub’s licensing guide!

