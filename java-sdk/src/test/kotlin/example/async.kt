package example

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.model.*
import com.github.bradynpoulsen.aws.coroutines.awsCoroutine

val asyncClient: AmazonEC2Async = TODO()

private suspend fun exampleAsync() {
    val result: DescribeReservedInstancesResult = awsCoroutine(asyncClient::describeReservedInstancesAsync)

    result.reservedInstances.forEach {
        println("${it.reservedInstancesId}: ${it.start..it.end} ${it.availabilityZone}")
    }
}

private suspend fun exampleAsyncWithRequestBuilder() {
    try {
        awsCoroutine(asyncClient::rebootInstancesAsync) {
            RebootInstancesRequest()
                .withInstanceIds("i-09593e1eca1212a29")
        }
    } catch (e: AmazonEC2Exception) {
        println("Failed to reboot EC2 instance!")
    }
}
