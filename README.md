# Kotlin Coroutines for AWS Java SDKs

Provides coroutine builders to execute AWS Java SDK commands as asynchronous or blocking via `awsCoroutine` and
`awsBlockingCoroutine` respectively.

## Example

```kotlin
val client = getAwsEc2AsyncClient()

// Launch a new instance for every custom AMI in the current account
coroutineScope {
    // suspends until the SDK command has completed
    awsCoroutine(asyncClient::describeImagesAsync) {
        DescribeImagesRequest()
            .withOwners("self")
    }.images.map { image ->
        // launch SDK commands in parallel
        async {
            awsCoroutine(asyncClient::runInstancesAsync) {
                RunInstancesRequest(image.imageId, 1, 1)
            }
        }
    }.awaitAll()
}
```

## Installation

This library is available via JitPack:

```kotlin
repositories {
    maven("https://jitpack.io")
}
```

Add it as a project dependency:

```kotlin
dependencies {
    implementation("com.github.bradynpoulsen", "aws-sdk-kotlin-coroutines", awsCoroutinesVersion)
}
```
