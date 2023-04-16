@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

configurations.configureEach {
    if (isCanBeConsumed) {
        attributes {
            attribute(
                GradlePluginApiVersion.GRADLE_PLUGIN_API_VERSION_ATTRIBUTE,
                objects.named("7.0")
            )
        }
    }
}

plugins {
    java
    kotlin("jvm") version "1.8.20"
    signing
    `maven-publish`
}

group = "com.github.rperez93"
version = "2.0"

repositories {
    google()
    mavenCentral()
}


dependencies {
    implementation(gradleApi())
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
    testImplementation("junit", "junit", "4.12")
}

extensions.getByType(JavaPluginExtension::class.java).apply {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.getByName<Jar>("jar") {
    manifest {
        attributes(
            Pair("Manifest-Version", "1.0"),
            Pair("Created-By", "Rafael Pérez")
        )
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "gradle-utils"
            from(components["java"])
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
    repositories {
        maven {
            name = project.properties["nexusName"] as String? ?: "remote"
            url = uri(
                project.properties["nexusURI"] as String?
                    ?: layout.buildDirectory.dir("repos/testRemote")
            )
            credentials {
                username = project.properties["nexusUsername"] as String? ?: "remote"
                password = project.properties["nexusPassword"] as String? ?: "password"
            }
        }
        maven {
            name = "local"
            url = uri(layout.buildDirectory.dir("repos/releases"))
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
