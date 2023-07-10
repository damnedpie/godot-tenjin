# godot-tenjin
Tenjin SDK 1.12.16 Android plugin for Godot. Built on Godot 3.5.2 artifact, written on Kotlin.

## Setup

Grab the``GodotTenjin`` plugin binary (.aar) and config (.gdap) from the releases page and put both into res://android/plugins. For easy start, you can also use my tenjin.gd script, it's methods are documented.

Make sure to open your Godot project, go to Project -> Settings and add a new "Tenjin/ApiKey" property (String). Store your ApiKey inside this property and reference it via ProjectSettings.get_setting("Tenjin/ApiKey").

Add to ``res://android/build/build.gradle`` in ``android -> defaultConfig``:
```
multiDexEnabled true
```
By default, the plugin uses all Appodeal dependencies and this should be just fine for most projects. If you want to cherry-pick your ad adapters, you should use the [Get Started Wizard](https://wiki.appodeal.com/en/android-beta-3-0-0/get-started) and change the dependencies in ``GodotAppodeal.gdap``.

It's recommended to follow other instructions from the Get Started page and tweak some parts of your Godot project's AndroidManifest.xml.

### AD-Id permission

It's mandatory to have the com.google.android.gms.ads.APPLICATION_ID permission in your AndroidManifest.xml for data attribution, so add the following permission to your project's AndroidManifest.xml:

```xml
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

Make sure to add it between `<!--CHUNK_USER_PERMISSIONS_BEGIN-->` and `<!--CHUNK_USER_PERMISSIONS_END-->` comments so it won't get deleted by Godot on export.

## Usage

Add the tenjin.gd as an Autoload to your project and use it's methods.

### Initialization

Tenjin has to be initialized via initializePlugin(apiKey) method. Facebook deeplink is made to be obtainable from NativeLib Facebook addon, but it's easy to modify to obtain it your way if you want.


## Building

If you want to rebuild the plugin, just run ``.\gradlew build`` from plugin project root directory. Make sure to provide actual Godot build template (godot-lib.release.aar) for the engine version you are using at ``godottenjin\libs`` folder.

Gradle buildscript will automatically put fresh plugin AAR to the ``/release`` folder and modify the GDAP. If you want to change TenjinSDK version, replace the tenjin.jar with desired jar in ``godottenjin\libs`` folder. Tenjin's version name is to be modified manually in plugin's build.gradle.
