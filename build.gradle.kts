import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id ("io.qameta.allure") version "2.8.1"
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
    implementation("com.consol.citrus:citrus-java-dsl:2.8.0")
    implementation("com.consol.citrus:citrus-http:2.8.0")
    implementation("com.consol.citrus:citrus-websocket:2.8.0")
    testImplementation("org.testng:testng:7.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:jcl-over-slf4j:1.7.25")
    testImplementation("org.slf4j:slf4j-log4j12:1.7.25")

    implementation("io.qameta.allure:allure-testng:2.17.2")
}

tasks.test {
    useJUnitPlatform()
    useTestNG()

    allure {
        autoconfigure = true
        aspectjweaver = true
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"


}

application {
    mainClassName = "MainKt"
}

