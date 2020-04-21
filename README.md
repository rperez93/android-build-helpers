# Gradle Utils Plugin 
![GitHub release (latest by date)](https://img.shields.io/github/v/release/rperez93/gradle-utils?color=blue&label=last%20release&logo=github&logoColor=white) ![Build Status](https://travis-ci.org/rperez93/gradle-utils.svg?branch=master)


The idea of this library is provide a set of plugins that allow easy configurations of Android gradle files.

## Library usage
```groovy
buildscript {
     repositories {
         jcenter()
     }
     dependencies {
         //sometimes the gradle plugin resolve the org.eclipse.jgit dependency "incomplete"
         // gets fixed putting the dependency manually in your classpath
         classpath 'org.eclipse.jgit:org.eclipse.jgit:5.4.2.201908231537-r'
         classpath 'com.github.rperez93:gradle-utils:1.5.0'
     }
 }
```

## Current Plugins

### utils-version-from-git-android

Get the last version tag from git and use the tag description as `versionName` for the `com.android.application` 
plugin or the `com.android.dynamic-feature` plugin.

#### Usage
Paste the next line of code just before applying the `com.android.application` plugin 
or the `com.android.dynamic-feature` plugin.
```groovy
apply plugin: 'utils-version-from-git-android'
```

## Contribute, feature request and ideas
Anyone can do a pull request, i will try to verify the pull request the more quickly i can. 

Any idea or suggestion is welcome.
