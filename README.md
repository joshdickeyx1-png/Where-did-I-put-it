# Where Did I Put It?

An offline-first Android app for saving and finding the things you put away.

## What it does

- Saves an item name, location, optional note, and optional photo.
- Searches across all of those details as you type.
- Lets people speak a note instead of typing it.
- Keeps everything locally on the device. There is no account, tracker, or cloud upload.

## Build it

Open this folder in current Android Studio and let it install the Android SDK/build tools it requests. Then select **Build > Generate Signed Bundle / APK** and choose **Android App Bundle** for Play Store upload.

For GitHub builds, push this project to a repository. The included workflow builds a release APK and AAB, available under the workflow run's artifacts.

## Before publishing

1. Replace `com.joshuadickey.findit` with your own unique package name.
2. Create a signing key and securely keep it; Google Play updates must use the same key.
3. Add a launcher icon, screenshots, privacy-policy page, Play Store description, and support email.
4. Set up Google Play's required Data safety answers truthfully: version 1 stores all item data only on-device.
