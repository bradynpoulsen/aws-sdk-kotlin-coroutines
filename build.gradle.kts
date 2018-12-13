import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.11" apply false
}

allprojects {
    group = "com.github.bradynpoulsen"
    version = "SNAPSHOT"

    repositories {
        jcenter()
    }

    tasks.withType(KotlinCompile::class.java).all {
        kotlinOptions.jvmTarget = "1.8"
    }
}
