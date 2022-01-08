import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.5.31"
    application
}

group = "me.archie"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.0")
    testImplementation(kotlin("test"))

    implementation("com.consol.citrus:citrus-core:2.8.0")
    implementation("com.consol.citrus:citrus-http:2.8.0")
    implementation("com.consol.citrus:citrus-websocket:2.8.0")
    testImplementation("org.testng:testng:7.4.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}

application {
//    mainClassName = "MainKt"
}