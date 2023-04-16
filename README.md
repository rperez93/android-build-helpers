# Gradle Utils Plugin 
![GitHub release (latest by date)](https://img.shields.io/github/v/release/rperez93/gradle-utils?color=blue&label=last%20release&logo=github&logoColor=white)
[![Java CI with Gradle](https://github.com/rperez93/gradle-utils/actions/workflows/gradle.yml/badge.svg)](https://github.com/rperez93/gradle-utils/actions/workflows/gradle.yml)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frperez93%2Fgradle-utils.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Frperez93%2Fgradle-utils?ref=badge_shield)


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
         // if you don't have that issue don't add the jgit classpath dependency
         classpath 'org.eclipse.jgit:org.eclipse.jgit:5.4.2.201908231537-r'
         
         classpath 'com.github.rperez93:gradle-utils:{last_version}'
     }
 }
```

Replace `{last_version}` with the last release number

## Current Plugins

### project-version-from-git

When this plugin is applied, it will add the `projectVersionFromGitTag` extension that contains the follow properties:
```
VersionName:  Extracted version from last github tag.
currentDateInVersionFormat: current date in format YYYYMMDD.
versionCodeLastDigit: used to complete the version code, by default is 0, it can be configured in the gradle build script.
versionCode: concatenation of currentDateInVersionFormat + versionCodeLastDigit
```


#### Usage
Apply the plugin before any other build plugin. Ex: before `com.android.application` plugin
```groovy
apply plugin: 'project-version-from-git'
```

Example usage of `projectVersionFromGitTag` extension in an Android project:

```groovy
// Using date as versionCode and git tag as versionName
versionCode projectVersionFromGitTag.versionCode
versionName projectVersionFromGitTag.versionName
```


### utils-version-from-git-android

- **Replaced by `project-version-from-git` since version 2.0**


## Contribute, feature request and ideas
Anyone can do a pull request, I will try to verify the pull request the more quickly i can. 

Any idea or suggestion is welcome.


## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frperez93%2Fgradle-utils.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Frperez93%2Fgradle-utils?ref=badge_large)
