# Godot Tenjin 1.16.5
[![Godot](https://img.shields.io/badge/Godot%20Engine-3.6-blue?style=for-the-badge&logo=godotengine&logoSize=auto)](https://godotengine.org/)
[![Tenjin](https://img.shields.io/badge/Tenjin_3.3.2-%230271c3?style=for-the-badge)](https://tenjin.com/)
[![GitHub License](https://img.shields.io/github/license/damnedpie/godot-tenjin?style=for-the-badge)](#)
[![GitHub Repo stars](https://img.shields.io/github/stars/damnedpie/godot-tenjin?style=for-the-badge&logo=github&logoSize=auto&color=%23FFD700)](#)

Appodeal SDK 3.3.2 Android plugin for Godot. Built on Godot 3.6 AAR.

## Setup

### Project integration

Grab the``GodotTenjijn`` plugin binary (.aar) and config (.gdap) from the releases page and put both into ``res://android/plugins``. For easy start, you can also use my ``GodotTenjin.gd`` script (it's very well documented).

Make sure to open your Godot project, go to Project -> Settings and add a new ``"Tenjin/AppKey" property (String)``. Store your Tenjin AppKey inside this property and reference it via ``ProjectSettings.get_setting("Tenjin/AppKey")``.

### AD-ID permission

It's mandatory to have the ``com.google.android.gms.ads.AD_ID`` permission in your ``AndroidManifest.xml``. Without this permission attribuition SDKs can't operate so add the following permission to your project's ``AndroidManifest.xml``:

```xml
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

Make sure to add it between `<!--CHUNK_USER_PERMISSIONS_BEGIN-->` and `<!--CHUNK_USER_PERMISSIONS_END-->` comments so it won't get deleted by Godot on export.

Alternatively, you can add this permission in the Android export preset (custom permissions list).

## Usage

Add the ``GodotTenjin.gd`` as an Autoload to your project and use its methods, they are all well commented.

### Initialization

Tenjin has to be initialized via ``initialize(appKey, deeplinkURI, appStoreType)`` method. You can leave ``deeplinkURI`` be an empty string if you don't have it; app store types are represented as an enum in the ``GodotTenjin.gd``.


## Building

If you want to rebuild the plugin, just run ``.\gradlew build`` from plugin project root directory. Make sure to provide actual Godot build template ``(godot-lib.release.aar)`` for the engine version you are using at ``godottenjin\libs`` folder.

For more comfort, you can run ``buildIntoRelease`` task so that the artifact is moved into ``/release`` folder after it's built.
