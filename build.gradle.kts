import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "ru.senin.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)


    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.5.31")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.0")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.32")

    testImplementation ("org.jetbrains.kotlin:kotlin-test:1.5.31")
    testImplementation ("org.jetbrains.kotlin:kotlin-test-junit:1.5.31")
    testImplementation ("junit:junit:4.13.2")

    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0") //for JVM platform
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
}


compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
