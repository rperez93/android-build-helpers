import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    maven
    kotlin("jvm") version "1.3.50"
}



group = "com.github.rperez93"
version = "1.0.0-rc1"

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

tasks.getByName<Upload>("uploadArchives") {

    repositories {

        withConvention(MavenRepositoryHandlerConvention::class) {

            mavenDeployer {
                println(mavenLocal().url)
                withGroovyBuilder {
                    "repository"("url" to uri(mavenLocal().url))
                }
            }
        }
    }
}