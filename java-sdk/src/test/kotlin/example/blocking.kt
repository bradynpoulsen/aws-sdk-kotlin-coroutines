package example

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.github.bradynpoulsen.aws.coroutines.awsBlockingCoroutine
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

val blockingClient: AmazonEC2 = TODO()
val customDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()

private suspend fun exampleBlocking() {
    val result = awsBlockingCoroutine(blockingClient::describeReservedInstances)

    result.reservedInstances.forEach {
        println("${it.reservedInstancesId}: ${it.start..it.end} ${it.availabilityZone}")
    }
}

private suspend fun exampleBlockingWithRequestBuilder() {
    val result = awsBlockingCoroutine(blockingClient::describeImages, context = customDispatcher) {
        DescribeImagesRequest()
            .withImageIds("ami-12312412431754", "ami-12334237461523")
    }

    result.images.forEach {
        println("${it.imageId}: ${it.description}")
    }
}
