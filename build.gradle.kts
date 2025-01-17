import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.invoke

val ktorVersion: String by project
val dateTimeVersion: String by project

val spaceUsername: String? by project
val spacePassword: String? by project
val spaceMavenRepositoryUrl: String by project
val sonatypeUsername: String by project
val sonatypePassword: String by project
val sdkVersion: String by project

group = "com.jeliuc"
version = System.getenv("SDK_VERSION") ?: sdkVersion

plugins {
    kotlin("jvm").version("1.9.20")
    kotlin("plugin.serialization").version("1.9.20")

    id("org.jlleitschuh.gradle.ktlint").version("12.0.3")
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.jetbrains.dokka-javadoc") version "2.0.0"
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"

    `maven-publish`
    `kotlin-dsl`
    signing
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    dokkaHtmlPlugin("org.jetbrains.dokka:versioning-plugin:2.0.0")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:$dateTimeVersion")
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
    java {
        withJavadocJar()
    }
}

sourceSets.main {
    kotlin.srcDirs("src/main/kotlin")
}

sourceSets.test {
    kotlin.srcDirs("src/test/kotlin")
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:2.0.0")
        classpath("org.jetbrains.dokka:versioning-plugin:2.0.0")
    }
}

dokka {
    moduleName.set("Turso API SDK")
    dokkaSourceSets.main {
    }

    pluginsConfiguration {
        version = "0.3.0"
        html {
            footerMessage.set("(c) 2025 Jeliuc.com")
        }
    }
}

tasks.named<Jar>("javadocJar") {
    from(tasks.named("dokkaGenerate"))
}

tasks.named("signCentralPortalPublication") {
    dependsOn("javadocJar")
}

gradlePlugin { isAutomatedPublishing = false }
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri(spaceMavenRepositoryUrl)
            name = "space"
            credentials {
                username = System.getenv("SPACE_USERNAME") ?: spaceUsername
                password = System.getenv("SPACE_PASSWORD") ?: spacePassword
            }
        }
    }
}

signing {
    useGpgCmd()
}

centralPortal {
    username = sonatypeUsername
    password = sonatypePassword
    name = "turso-sdk-jvm"

    pom {
        name = "Turso Platform SDK"
        description = "Kotlin library for developing applications using Turso Platform API."
        url = "https://github.com/Jeliuc-Labs/turso-sdk"
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id = "a2xchip"
                name = "Alexandr Jeliuc"
                email = "alex@jeliuc.com"
                organization = "Jeliuc.com S.R.L."
                organizationUrl = "https://jeliuc.com"
            }
        }

        scm {
            connection.set("scm:git:git://github.com/Jeliuc-Labs/turso-sdk.git")
            developerConnection.set("scm:git:ssh://github.com/Jeliuc-Labs/turso-sdk.git")
            url = "https://github.com/Jeliuc-Labs/turso-sdk"
        }
    }
}
