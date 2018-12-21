package example

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.model.AmazonEC2Exception
import com.amazonaws.services.ec2.model.DescribeReservedInstancesResult
import com.amazonaws.services.ec2.model.RebootInstancesRequest
import com.github.bradynpoulsen.aws.coroutines.suspendCommandAsync

val ec2AsyncClient: AmazonEC2Async = TODO()

private suspend fun exampleAsync() {
    val result: DescribeReservedInstancesResult = suspendCommandAsync(ec2AsyncClient::describeReservedInstancesAsync)

    result.reservedInstances.forEach {
        println("${it.reservedInstancesId}: ${it.start..it.end} ${it.availabilityZone}")
    }
}

private suspend fun exampleAsyncWithRequestBuilder() {
    try {
        suspendCommandAsync(ec2AsyncClient::rebootInstancesAsync) {
            RebootInstancesRequest()
                .withInstanceIds("i-09593e1eca1212a29")
        }
    } catch (e: AmazonEC2Exception) {
        println("Failed to reboot EC2 instance!")
    }
}
