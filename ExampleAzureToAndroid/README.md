# HOW TO START

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
