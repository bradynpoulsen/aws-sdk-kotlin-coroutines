import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    maven
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(coroutines("jdk8"))
    implementation(awsSdk("core"))
}

dependencies {
    testImplementation(awsSdk("ec2"))
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions.freeCompilerArgs += setOf(
        "-Xuse-experimental=kotlin.Experimental",
        "-Xuse-experimental=com.github.bradynpoulsen.aws.coroutines.InternalAwsCoroutineApi"
    )
}

fun DependencyHandler.coroutines(name: String, version: String = project.extra["coroutines_version"] as String) = "org.jetbrains.kotlinx:kotlinx-coroutines-$name:$version"
fun DependencyHandler.awsSdk(name: String, version: String = project.extra["aws_sdk_version"] as String) = "com.amazonaws:aws-java-sdk-$name:$version"
