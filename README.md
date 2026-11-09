# Mobile App Automation | Learning & Project Portfolio

> I have learned and applied mobile app automation using **Java, Selenium WebDriver
> APIs, TestNG, and Appium**, and successfully automated workflows in three apps.
>
> This portfolio summarizes that experience and the reusable Android automation
> framework in this repository.

**Stack:** Java 21 · Maven · Appium 2 · Selenium · TestNG · Android · UiAutomator2

## Explore this README

- [Apps automated](#apps-automated)
- [What I learned](#what-i-learned)
- [How mobile automation works](#how-mobile-automation-works)
- [Finding and managing locators](#finding-and-managing-locators)
- [Framework architecture](#framework-architecture)
- [Run the Android automation suite](#run-the-android-automation-suite)
- [Project structure](#project-structure)
- [Troubleshooting](#troubleshooting)

<details>
<summary><strong>Apps automated — expand project notes</strong></summary>

| App | Description |
|---|---|
| **Conceptify AI** | AI application |
| **Marg** | Ikigai-based career guidance |
| **DhammaPath** | Vipassana meditation guide |

I successfully automated app workflows while learning and applying the same core
automation concepts across these projects.

</details>

## What I learned

- How to create and run Java tests with Maven and TestNG.
- How Appium lets Java use Selenium's WebDriver-style APIs to interact with mobile UI.
- How to start an Android session through Appium and UiAutomator2.
- How to inspect screens, choose stable locators, and keep them separate from tests.
- How to structure reusable tests using business workflows and screen objects.
- How to use explicit waits, environment-based settings, and failure screenshots.

## How mobile automation works

Selenium WebDriver is commonly used to automate web browsers. For native mobile
applications, **Appium** provides a WebDriver-compatible automation interface. The
Java Appium client uses Selenium APIs and communicates with an Appium server; on
Android, UiAutomator2 and ADB communicate with the device.

```text
Java + TestNG test
        ↓
Appium Java Client (WebDriver APIs)
        ↓
Appium Server
        ↓
UiAutomator2
        ↓
ADB
        ↓
Android app on emulator/device
```

This means Selenium skills such as locating elements, clicking, typing, and waiting
are useful, but **Selenium alone does not automate a native Android app**. Appium
provides the mobile driver. Web apps, native apps, and hybrid apps can require
different driver contexts and setup. Not every screen exposes automatable elements;
custom-drawn controls, secure screens, permissions, and system dialogs can need a
different approach.

## Finding and managing locators

You generally inspect a screen when adding a new locator or when an existing locator
breaks—not before every test. Inspect once, save a meaningful locator, and reuse it
through the object repository.

### Inspect an Android screen

1. Start an emulator or connect a device; check it with `adb devices`.
2. Start Appium and open **Appium Inspector**.
3. Connect Inspector to the Appium server with Android capabilities, including
   `platformName: Android` and `automationName: UiAutomator2`.
4. Launch the target app, select an element in the hierarchy, and inspect its
   attributes: resource ID, content description, text, hint, class, and bounds.
5. Choose the most stable locator available and save it under a logical key.
6. Verify it by running the test and update the repository if the app UI changes.

### Prefer stable locators

Use the first available option from this general preference order:

1. **Accessibility ID** — Android content description / accessibility identifier.
2. **Resource ID** — a stable app-provided view identifier.
3. **Class name** — useful when there is a unique matching element.
4. **Android UiAutomator selector** — an Android-specific query.
5. **XPath** — useful when other attributes are unavailable; keep it short and
   attribute-based.

For example, if an app exposes email and password fields by their `hint` and a Login
button by its `content-desc`, the locator repository can express them as:

```properties
username=xpath=//android.widget.EditText[@hint='Email']
password=xpath=//android.widget.EditText[@hint='Password']
loginButton=xpath=//android.widget.Button[@content-desc='Login']
```

An accessibility ID can be simpler when the app exposes one:

```properties
loginButton=accessibility_id=Login
```

Avoid positional XPath such as `/hierarchy/.../node[3]`: layout changes can make it
point to the wrong element. Prefer IDs and accessibility labels; use XPath only when
needed. Keep locators in
[`src/main/resources/mobile/`](src/main/resources/mobile/), not in test or business
classes.

<details>
<summary><strong>How a locator is used</strong></summary>

The test calls a business workflow; the business workflow calls screen actions; the
screen resolves keys from the locator repository and interacts through explicit waits:

```text
LoginTest
   → LoginBusiness
      → LoginScreen.enterUsername(...)
         → "username" locator key
            → Wait for element, then type
```

If a UI change alters the Email field hint, update the `username` entry and keep the
test workflow unchanged.

</details>

## Framework architecture

```text
Test
 ↓
Business workflow
 ↓
Screen object
 ↓
Object repository
 ↓
Core utilities and driver
 ↓
Appium → UiAutomator2 → Android app
```

The test describes a scenario and assertions. Business code describes reusable
workflows. Screen objects perform UI actions. The object repository stores locator
definitions. Core code manages configuration, driver creation, and waits.

## Run the Android automation suite

### Prerequisites

- JDK 21 and Maven 3.9+
- Node.js and Appium 2
- Android SDK Platform Tools and an emulator or device
- An Android APK, or an installed Android application that you are authorized to test

Install Appium and its Android driver:

```bash
npm install -g appium
appium driver install uiautomator2
```

Copy the safe template and configure the local environment:

```bash
cp env.example .env
```

Set the APK path (or installed app details) and **your authorized test-account
credentials** in `.env`. Never commit `.env` or real credentials. Use the package name,
launch activity, APK, and locators for the app you intend to test.

```dotenv
APP_PATH=/absolute/path/to/application.apk
APP_PACKAGE=your.app.package
APP_ACTIVITY=your.app.package.MainActivity
TEST_USERNAME=your_test_user@example.com
TEST_PASSWORD=your_test_password
DEVICE_NAME=emulator-5554
```

For an already installed app, `APP_PATH` may be left blank; set `APP_PACKAGE` and
`APP_ACTIVITY`.

Verify the device and start the Appium server in separate terminals:

```bash
adb devices
appium
```

Run the suite:

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

The sample login test launches the configured application, completes its configured
login workflow, and verifies the expected post-login screen. Adapt its test data,
locators, and screen actions to the application under test.

JVM properties override environment variables, which override properties files, which
override defaults. For example:

```bash
mvn clean test -Denvironment=qa -DplatformName=Android -DdeviceName=emulator-5554
```

## Project structure

```text
config/                          Environment and Android defaults
src/main/java/com/automate/
  business/                      Reusable user workflows
  config/                        Centralized configuration
  core/                          Driver, locators, waits, framework constants
  hooks/                         Test lifecycle helpers
  pages/mobile/                  Mobile screen objects
  reporting/                     TestNG listener and HTML report
  testdata/                      JSON reader and typed login data
  utils/                         Screenshots and common utilities
src/main/resources/mobile/       Mobile locator repositories
src/test/java/                   TestNG scenarios
test-data/                       Environment-neutral sample data
testng.xml                       Test suite entry point
```

## Reports and troubleshooting

HTML reports are written to `reports/`; failed-test screenshots are written to
`screenshots/`. Both folders are ignored by Git except for their `.gitkeep` files.

| Issue | What to check |
|---|---|
| Appium server unavailable | Start Appium; verify the URL and port in `.env` |
| Device not detected | Run `adb devices`; unlock the device and accept debugging prompts |
| ADB unauthorized | Reconnect the device and accept the USB debugging prompt |
| APK not found | Set an absolute, valid `APP_PATH` |
| App fails to launch | Confirm `APP_PACKAGE` and `APP_ACTIVITY` using Appium Inspector or `adb shell dumpsys package <package>` |
| Locator not found | Inspect the current screen in Appium Inspector and update the relevant repository key |
| UiAutomator2 failure | Check Appium logs, ADB connectivity, and the installed UiAutomator2 driver |

## Notes

- Use only apps, devices, and test accounts you are authorized to automate.
- Real credentials belong in the ignored `.env` or a secure secret manager—not source
  files, test-data JSON, screenshots, or commits.
- The framework currently demonstrates Android. iOS and cross-device execution are
  future extensions, not capabilities claimed by this example.
