package example

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.github.bradynpoulsen.aws.coroutines.suspendCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

val ec2Client: AmazonEC2 = TODO()

private suspend fun exampleBlocking() = coroutineScope {
    val result = suspendCommand(ec2Client::describeReservedInstances)

    result.reservedInstances.forEach {
        println("${it.reservedInstancesId}: ${it.start..it.end} ${it.availabilityZone}")
    }
}

private suspend fun exampleBlockingWithRequestBuilder() = coroutineScope {
    val result = suspendCommand(ec2Client::describeImages, context = Dispatchers.IO) {
        DescribeImagesRequest()
            .withImageIds("ami-12312412431754", "ami-12334237461523")
    }

    result.images.forEach {
        println("${it.imageId}: ${it.description}")
    }
}
