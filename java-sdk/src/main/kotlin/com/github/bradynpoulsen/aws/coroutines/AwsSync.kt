package com.github.bradynpoulsen.aws.coroutines

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Executes the synchronous [method] in the current [CoroutineScope]'s dispatcher.
 *
 * Example:
 *
 *   val client: AmazonEC2 = buildClient()
 *   val describeResult: DescribeInstancesResult = suspendCommand(client::describeInstances)
 */
@ExperimentalAwsCoroutineApi
@UseExperimental(ExperimentalCoroutinesApi::class)
suspend inline fun <AwsResult : AmazonWebServiceResult<*>> CoroutineScope.suspendCommand(
    crossinline method: () -> AwsResult,
    context: CoroutineContext = EmptyCoroutineContext
): AwsResult = withContext(newCoroutineContext(context)) {
    method()
}

/**
 * Executes the synchronous [method] in the current [CoroutineScope]'s dispatcher.
 *
 * Example:
 *
 *   val client: AmazonEC2 = buildClient()
 *   val describeResult: DescribeImagesResult = suspendCommand(client::describeImages) {
 *       DescribeImagesRequest()
 *          .withImageIds("ami-12312412431754", "ami-12334237461523")
 *   }
 */
@ExperimentalAwsCoroutineApi
@UseExperimental(ExperimentalCoroutinesApi::class)
suspend inline fun <AwsRequest : AmazonWebServiceRequest, AwsResult : AmazonWebServiceResult<*>> CoroutineScope.suspendCommand(
    crossinline method: (request: AwsRequest) -> AwsResult,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline requestBuilder: () -> AwsRequest
): AwsResult = withContext(newCoroutineContext(context)) {
    method(requestBuilder())
}
