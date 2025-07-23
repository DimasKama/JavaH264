# JavaH264

Java bindings for [OpenH264 library](https://www.openh264.org/) written in Rust using JNI.

## Usage

In your `build.gradle`:
```
repositories {
    // ... other repositories
    
    maven {
        name "KvotheShaed Releases"
        url "https://maven.kvotheshaed.ru/releases"
    }
}

dependencies {
    // ... other dependencies

    implementation "ru.dimaskama:javah264:${javah264_version}"
}
```

## Credits
- [OpenH264](https://www.openh264.org/)
- [openh264-rs](https://github.com/ralfbiedert/openh264-rs/)
- [jni-rs](https://github.com/jni-rs/jni-rs/)