# Automate Mobile

Production-oriented Android mobile automation framework built with Java 21, Maven,
Appium 2, UiAutomator2, and TestNG.

## Architecture

```text
Test -> Business -> Screen/Page -> Object Repository -> Core -> Appium -> Android
```

Tests invoke business workflows only. Screens own UI interactions and locator lookup;
the object repository contains all sample locator definitions.

## Prerequisites

- JDK 21
- Maven 3.9+
- Node.js
- Appium 2
- Android SDK Platform Tools and emulator tooling
- An Android emulator or physical device
- A test APK or an already installed application

## Installation

```bash
npm install -g appium
appium driver install uiautomator2
```

Copy the environment template and configure the target device/application:

```bash
cp env.example .env
```

Never commit `.env`; use Appium Inspector values rather than guessed production locators.

## Verify the Android environment

```bash
adb devices
adb shell getprop ro.build.version.release
```

## Start Appium

```bash
appium server --base-path /
```

If the server uses the default port, `http://127.0.0.1:4723` is already configured.

## Configuration precedence

Values are resolved in this order:

1. JVM/system property
2. Environment variable
3. `config/config.properties` or platform properties
4. Default value

Examples:

```bash
mvn clean test -Denvironment=qa -DplatformName=Android -DdeviceName=Pixel_9
```

## Run tests

```bash
mvn clean test
mvn clean test -DsuiteXmlFile=testng.xml
```

## Configuration

Edit `config/android.properties` for Android defaults. Supply the APK or installed-app
details through `.env`:

- APK supplied: set `APP_PATH=/absolute/path/application.apk`
- Installed application: set `APP_PACKAGE` and `APP_ACTIVITY`

The `.env.example` file lists all supported values without secrets.

## Sample locators

`src/main/resources/mobile/*Locators.properties` contains clearly marked sample
locators for `com.example.app`. Replace them with values from Appium Inspector after
selecting the real application. Supported strategies are:

- `id`
- `accessibility_id`
- `class_name`
- `android_uiautomator`
- `xpath`

Resource ID and accessibility ID are preferred over XPath.

## DhammaPath example

The framework is configured for the released DhammaPath Android APK with this package
and activity:

```text
Package:  com.rohitparsode.dhammapath
Activity: com.rohitparsode.dhammapath.MainActivity
```

Set the APK location and login credentials in the ignored `.env` file:

```dotenv
APP_PATH=/absolute/path/to/app-release.apk
APP_PACKAGE=com.rohitparsode.dhammapath
APP_ACTIVITY=com.rohitparsode.dhammapath.MainActivity
TEST_USERNAME=your_test_user@example.com
TEST_PASSWORD=your_test_password
DEVICE_NAME=emulator-5554
```

The covered login flow is:

1. Launch DhammaPath.
2. Tap `Next` twice.
3. Tap `Begin`.
4. Enter the test email and password.
5. Tap `Login`.
6. Verify the DhammaPath home screen.

## Reporting and screenshots

- Spark HTML reports are written to `reports/`
- Failed-test screenshots are written to `screenshots/`
- Driver lifecycle and screenshots are handled centrally by framework hooks/listeners

## Mobile automation model

Appium Java Client communicates with the Appium server, which uses the UiAutomator2
driver and ADB to control the Android application. Mobile automation does not require
a browser DOM.

## Troubleshooting

| Problem | Check |
|---|---|
| Appium server unavailable | Run `appium`, verify port 4723, and test the URL in `.env` |
| Device not detected | Run `adb devices`; unlock the device and accept debugging prompts |
| ADB unauthorized | Revoke USB authorizations and reconnect the device |
| APK not found | Confirm `APP_PATH` is absolute and the file exists |
| Incorrect package/activity | Verify values with `adb shell dumpsys package <package>` or Appium Inspector |
| Locator not found | Refresh Appium Inspector values and update the locator properties |
| UiAutomator2 failure | Restart ADB/Appium and reinstall the UiAutomator2 driver |
