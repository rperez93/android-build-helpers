@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.bmuschko:gradle-nexus-plugin:2.3.1")
    }
}

plugins {
    java
    maven
    `maven-publish`
    kotlin("jvm") version "1.3.50"
    id("com.bmuschko.nexus") version "2.3.1"
}

group = "com.github.rperez93"
version = "1.7"


repositories {
    google()
    jcenter()
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.4.2.201908231537-r")
    testImplementation("junit", "junit", "4.12")

}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.getByName<Jar>("jar") {
    manifest {
        attributes(
            Pair("Manifest-Version", "1.0"),
            Pair("Created-By", "Rafael Pérez")
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("Gradle Utils")
                description.set("Collection of simple Gradle utilities.")
                url.set("https://github.com/rperez93/gradle-utils")
                inceptionYear.set("2019")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("rperez93")
                        name.set("Rafael Pérez")
                        email.set("perez.rafael1993@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:https://github.com/rperez93/gradle-utils.git")
                    developerConnection.set("scm:https://github.com/rperez93/gradle-utils.git")
                    url.set("https://github.com/rperez93/gradle-utils")
                }
            }
        }
    }
}

apply(from = "nexus.gradle")
