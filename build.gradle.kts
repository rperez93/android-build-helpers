import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.kotlin.dsl.kotlin

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
    id("com.gradle.build-scan") version "2.0.2"
}

group = "com.github.rperez93"
version = "1.0.0"

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("My Library")
                description.set("A concise description of my library")
                url.set("http://www.example.com/library")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("johnd")
                        name.set("John Doe")
                        email.set("john.doe@example.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://example.com/my-library.git")
                    developerConnection.set("scm:git:ssh://example.com/my-library.git")
                    url.set("http://example.com/my-library/")
                }
            }
        }
    }
}

apply(from = "nexus.gradle")
