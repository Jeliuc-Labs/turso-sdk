import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin

val ktorVersion: String by project

val spaceUsername: String? by project
val spacePassword: String? by project
val clientVersion: String by project

group = "com.jeliuc"
version = clientVersion

plugins {
    kotlin("jvm").version("1.9.20")
    id("org.jlleitschuh.gradle.ktlint").version("12.0.3")
    kotlin("plugin.serialization").version("1.9.20")

    id("org.jetbrains.dokka") version "1.9.10"

    `maven-publish`
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

val dokkaPlugin by configurations

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    dokkaHtmlPlugin("org.jetbrains.dokka:versioning-plugin:1.9.10")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-resources:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
}

tasks.test {
    testLogging.showStandardStreams = true
    testLogging.exceptionFormat = TestExceptionFormat.FULL
    useJUnitPlatform()
}

ktlint {
    version.set("1.1.1")
}

kotlin {
    jvmToolchain(17)
}

sourceSets.main {
    kotlin.srcDirs("src/main/kotlin")
}

sourceSets.test {
    kotlin.srcDirs("src/test/kotlin")
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.10")
        classpath("org.jetbrains.dokka:versioning-plugin:1.9.10")
    }
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set("Turso API SDK")
    suppressObviousFunctions.set(true)
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "(c) 2024 Jeliuc.com"
    }
}

tasks.dokkaHtml {
    pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        version = "0.2.0"
        olderVersionsDir = file("documentation/version")
        renderVersionsNavigationOnAllPages = true
    }
}
