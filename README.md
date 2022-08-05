# KSW-ToolKit

![screenshot](/images/screenshot-eventmanager.png)

KSW-ToolKit is an extension to your KSW Android screen. Once the service is installed, it will hijack the original McuService and will work as replacement which you can highly customize. This service allows you to remap your buttons to invoke various Android keystrokes, start any App of your desire or invoke various MCU hardware commands like switching sources or tuning the brightness. The service also bypasses various inconveniances the original McuService caused, like not hearing your Android when you're in OEM side, keeping your Apps open when you switch sources or generally having the Android react faster to your hardware inputs.

...in short KSW-ToolKit
## Features:
* Rich customizability
* Remapping of your hardware buttons
* Automatic Day/Night switching
* Auto Volumes
* Sound Restorer to keep sound running on OEM side
* Enables extra hardware buttons*
* Built in ADB Shell to manipulate and have more control over your Android
* And much more to discover...

*This is the "Media-Button Hack". It will depend on the car and MCU model you have. In BMWs for example this hack will allow the Tel and Media/CD button on your iDrive to also get triggered as Phone and MODE event in Android. Check MCU Listener if this hack works for you.

## Installation:
Under the releases tab on the right, you'll find the latest version. Download the APK and install this on your Android. You can also download the APK and copy the package to an USB and install this from the built in file manager (usually ES File Explorer). Once installed the App will prompt you to install the needed service. Should the installation fail, you can manually download this from here as well: [KSW-ToolKit-Service](https://github.com/Snaggly/KSW-ToolKit-Service/releases/latest).

## Building

Android Studio was used to write and compile this App. For the core functionalities to work (eg. invoking KeyEvents on Instrumentation), this App requires System Level priviledges. When compiling, you will need to sign the APK with the same KeyStore as the Android OS was signed with.

## Contributing

Contributions of any sorts is highly welcome! To contribute open a new pull request or open an issue to discuss new ideas.

## Credits

* [McuCommunicator](https://github.com/KswCarProject/McuCommunicator) - To communicate to Mcu
* [AdbLib](https://github.com/cgutman/AdbLib) - To access the Adb shell

## License

* [GPL v3.0](https://choosealicense.com/licenses/gpl-3.0/)
