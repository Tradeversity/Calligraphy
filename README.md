Colorography
===========
A way to custom/dynamically theme your application at inflation so you don't have to manually change the color tinting of controls

## Getting started

### Dependency

Include the dependency

```groovy
dependencies {
    compile 'com.52inc:colorography:3.0.0'
    
    // Snapshot
    compile 'com.52inc:colorography:3.0.0-SNAPSHOT'
}
```

### Usage

```xml
<*View themeColor="@color/backup_theme_color" />
``` 
**Note: The missing namespace, this __IS__ intentional.**

### Installation

Define your default font using `ColorographyConfig`, in your `Application` class in the `#onCreate()` method.

```java
@Override
public void onCreate() {
    super.onCreate();
    ColorographyConfig.initDefault(new ColorographyConfig.Builder()
          	.setThemeColorAttrId(R.attr.themeColor)
          	.build());
    //....
}
```

_Note: You don't need to define `ColorographyConfig` but the library will apply
no default font and use the default attribute of `R.attr.themeColor`._

### Inject into Context

Wrap the `Activity` Context:

```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ColorographyContextWrapper.wrap(newBase, themeColorProvider));
}
```

_You're good to go!_


## Usage

### Custom font per TextView

```xml
<TextView
    android:text="@string/hello_world"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    themeColor="@color/colorAccent"/>
```

_Note: Popular IDE's (Android Studio, IntelliJ) will likely mark this as an error despite being correct. You may want to add `tools:ignore="MissingPrefix"` to either the View itself or its parent ViewGroup to avoid this. You'll need to add the tools namespace to have access to this "ignore" attribute. `xmlns:tools="
http://schemas.android.com/tools"`. See https://code.google.com/p/android/issues/detail?id=65176._



# Collaborators

- [@mironov-nsk](https://github.com/mironov-nsk)
- [@Roman Zhilich](https://github.com/RomanZhilich)
- [@Smuldr](https://github.com/Smuldr)
- [@Codebutler](https://github.com/codebutler)
- [@loganj](https://github.com/loganj)
- [@dlew](https://github.com/dlew)
- [@ansman](https://github.com/ansman)

# Note

This library was created because it is currently not possible to declare a custom font in XML files in Android.

If you feel this should be possible to do, please star [this issue](https://code.google.com/p/android/issues/detail?id=88945) on the official Android bug tracker.

# Licence

    Copyright 2013 Christopher Jenkins
    Copyright 2017 52inc
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
