# Westerra Credit Union retail banking application

This is the code base for the Westerra Credit Union retail banking Android application. It is based on the Backbase mobile
Journey architecture. The Backbase retail Journey release version on which this project is based is recorded in
[gradle.properties](gradle.properties).

## Developer Environment Setup - Mac

Install latest version of these:

1. [Android Studio](https://developer.android.com/studio) Jellyfish or later
2. [Java JDK 19](https://www.oracle.com/java/technologies/downloads/#jdk19-mac)
3. [Homebrew](https://brew.sh/)
4. [Fastlane](https://docs.fastlane.tools/getting-started/ios/setup/)
5. [Ktlint](https://pinterest.github.io/ktlint)
6. [RVM](https://rvm.io/) `rvm use ruby-2.7.6`

## Backbase Setup

[Backbase.io Setup Android Development](https://backbase.io/developers/documentation/mobile-devkit/getting-started/set-up-android-development/)

This project depends on various artifacts published to repositories on [Backbase Repo](https://repo.backbase.com). You
must have read access to these repositories to build this project. To ensure the build system can access these
artifacts on your machine, follow these steps:

1. Log in to [Repo](https://repo.backbase.com) and click your name in the top right.
2. Type in your password to get access to your encrypted password. Copy your encrypted password.
3. Open/create the `gradle.properties` file at `/Users/<username>/.gradle/` (Mac) /
   `C:\Users\<username>\.gradle\` (Windows) / `/home/<username>/.gradle/` (Linux).
4. Add `backbaseRepoUsername=<your username>` to the file.
5. Add `backbaseRepoEncryptedPassword=<your encrypted password>` to the file.

Note that this process should **not** result in your password being added to source control. Each developer of this
project must do this individually.

## Release Process
- Checkout latest `develop` branch
- Create a release branch `git checkout -b release/2.2407.0`
- Update version the version string in build.gradle `2.2407.0` 
- Commit the updated version
- Create a PR from `release/2.2407.0` to `develop` branch
- Go through the CircleCI pipeline and build DEV, UAT, and PROD
- Do a full app regression testing with QA and new features/bug fixes
- Once approved, build "Play Store prod release upload using Fastlane" using CircleCI.
- Test the build on a real device and get the updated build through the Google Play Store Beta program on the device.
- Play Store Review Submission:
   - Go to Play Store Console -> Westerra Credit Union -> Testing -> Internal Testing
   - Go to Releases -> (build) version -> 'promote release'->'production' for this version (ie. 2.2407.0)
   - Scroll down to the Release Notes and either click to 'copy from previous release' and either use the standard release notes or make custom notes for this release about important changes.
   - Click next to go to the next screen
   - Scroll down to Roll-out percentage and decide if you want to do a staged roll out or just go straight to 100%.
   - Click Save to go to the release summary and final screen
   - Click to Submit for review if everything looks good
- Play Store Release:
   - Once approved, the app will automatically be released and you will get an email notification.
   - If you choose a staged release, you can go back in and edit the release and change the roll-out percentage gradually until you reach 100%. It's a manual process and you have to come back in to change the value for each increment.  Additional reviews are not required and the change goes into effect immediately.

## Sandbox environment with latest Android build

It's helpful to run the latest Android build against the Sandbox environment to see the latest changes 
and troubleshoot issues.

[Backbase.io Sandbox Android Guide](https://backbase.io/developers/documentation/api-sandbox/retail-banking-usa/android-guide/)

1. Be sure to request a Sandbox API key ahead of time as it can take overnight for the Backbase email support to respond. The key is common for Westerra Retail and Business so you can also ask a team member to share it with you.
2. Follow the docs to download latest Android SDK, configure and build.
3. Be sure to copy the debug.keystore as described in the documentation and backup the existing debug.keystore before putting the sandbox keystore in place. Swap the keystore back after done working in Sandbox before returning to Westerra Android project. If the keystore is wrong, you won't be able to login but the app will build and launch.
4. Use [Sandbox account credentials](https://backbase.io/developers/documentation/api-sandbox/retail-banking-usa/retail-user-credentials/) to login and test with.

## AWS WAF SDK

A third party package for AWS WAF is included in local folder /app/aws-waf-sdk and needs to have 
it's aar file loaded into your local maven repository to build the project with the following command:

```
./gradlew publishToMavenLocal
```

This only needs to be done once on initial environment setup or if the sdk is updated.

## Ktlint

```Autocorrect style violations
ktlint --code-style=android_studio --format
```

## CI/CD

Login to these tools using your github account and request access to the westerra project.

1. [Circle Ci](https://app.circleci.com/pipelines/github/westerra)
2. [AppCenter](https://appcenter.ms/)

Run all unit tests on all build flavors:
```
fastlane test
```

### Automated Build Versioning

The app build's Version and Build numbers are set at compile time. Build version commits are not
necessary. The expected build process uses the CircleCi pipeline and builds are 
promoted from Dev->Uat->Prod environments. Each environment step compiles the appropriate 
Version/Build number sequentially at the time the build is promoted.  Builds may not be promoted
and go reset back to Dev environment at any time. It's also possible a developer would make a build 
on their local computer.

The compile time logic used to determine the Version/Build number and support these different 
situations is as follows:

### Fastlane App Distribution Local Setup
1. Get a copy of `firebase-app-distribution-private-key.json`
2. Store it somewhere safe locally, i.e in `~/.ssh` folder
3. Add the following to `~/.zshrc`
   - `export GOOGLE_APPLICATION_CREDENTIALS=~/.ssh/firebase-app-distribution-private-key.json`
   - Save and `source ~/.zshrc`
4. Ensure the environment variable is set up correctly by running `echo $GOOGLE_APPLICATION_CREDENTIALS`

### Distribute To Fastlane From Local
1. Set `OVERRIDE_BUILD_NUM` environment variable with desired build version number
2. Run the corresponding command to build and upload to Firebase App Distribution:
   - DEV: `fastlane upload_dev_to_firebase`
   - UAT: `fastlane upload_uat_to_firebase`
   - PROD: `fastlane upload_prod_to_firebase`

#### Version Number

1. If OVERRIDE_VERSION environment variable exists use it's value.
2. Make an api query to AppCenter to get last Dev and Prod environment. If building Dev environment
and Dev and Prod Version/Build numbers are the same, increment the minor build number (0.0.1 to 0.0.2).
If building other environments, use the version of the last Dev build.
3. The default value hardcoded build.gradle file.

#### Build Number

1. If OVERRIDE_BUILD_NUM environment variable exists use it's value.
2. Make an api query to AppCenter to get last Dev and Prod environment. If building Dev environment,
increment the last Dev environments Build number (123 -> 124). For all other environments, use the
same Build number as the last Dev build.
3. If CIRCLE_BUILD_NUM environment variable exists use it's value. CircleCi automatically manages
this value. It is just a backup in-case #1 and #2 fail for some reason.
4. The default value hardcoded build.gradle file.

## Forced Update

Forced Update behavior is controlled using Firebase Remote Config feature. There are two keys used
'minimum_version' and 'minimum_version_qa'.  The QA environments (dev/sit/uat) share use of
'minimum_version_qa' while prod uses 'minimum_version'. The values are JSON strings with the
following values and the app will show the Update Required Alert as needed:

```
minimum_version_android: X.X.X
minimum_version_ios: X.X.X
minimum_version_title: Set to override the default alert title.
minimum_version_message Set to override the default alert message.
```

## Backbase Support Version Updates

Here are instructions and notes about updating the Android project to a more recently release 
Backbase Support Version. The backend upgrade wll cause the Backbase API calls to be updated and 
sometimes it will be incompatible until Android's Backbase dependencies are updated to a version
that works with the API. Updating the Android Backbase dependencies may result in customizations
built on top of those dependencies to break and need updates. The Android changes would likely
be done after backend changes or in parallel as an updated backend will need to be available to 
complete the Android upgrade process.

1. Review the Android Backbase documentation release notes for the new version: 
(Backbase Release Notes)[https://backbase.io/developers/documentation/release-notes/]
2. Download the Android kickstart project for the intended Backbase support version.
3. Update Android Backbase dependencies to the target support versions. Recommended way to do this
is to use the kickstarter project to find the expected version.  Go through each dependency
in the build.gradle and commonDependencies.gradle file and set them to be at or above the 
kickstarter app's versions.
4. Look for new dependencies added in the kickstart app that are not included currently and include 
them.
5. Look for any other project structure changes in the kickstarter app and sync to them. 
6. Go through every file in the res/layouts and res/nav directory and sync it's changes with the 
latest version from the associated Backbase dependency.
7. Try to build the project. Run against an environment that has had the backend API updates done.
Fix any build issues resulting from dependency updates. Get the build working again. 
8. Verify you can log into the app and do a smoke test for run time errors. Fix runtime errors.
9. Look for customizations that were working before the upgrade that were lost and fix them.
10. For each dependency, see if there is a minor version patch update available by searching the
[JFrog Repo](https://repo.backbase.com/ui/packages). Try making the updates and smoke test the app
again.
11. Review the Backbase release notes documentation again from step #1 and make any changes needed.
12. Look for code using deprecated Backbase dependencies and fix it to not use the deprecated code.
13. Run the Android project tests and fix failures.
14. Run the Mobile Automation tests against the functional app. Resolve failures by updating the app
to work as expected again or modifying the automation test to work with the changes.
