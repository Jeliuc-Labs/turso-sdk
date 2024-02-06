import org.gradle.api.tasks.testing.logging.TestExceptionFormat

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

    `maven-publish`
    `kotlin-dsl`
}

repositories {
    // Use Maven Central for resolving dependencies.
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")

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
    verbose.set(true)
    debug.set(true)
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
