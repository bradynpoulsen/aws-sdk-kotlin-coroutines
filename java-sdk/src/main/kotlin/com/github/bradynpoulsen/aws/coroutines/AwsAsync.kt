package com.github.bradynpoulsen.aws.coroutines

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.handlers.AsyncHandler
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.cancelFutureOnCancellation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Future
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Starts a [CancellableContinuation] that executes [method] and resumes with the [AwsResult].
 *
 * _Note_: This request is launched in the SDK client's executor threads.
 * See [com.amazonaws.client.builder.AwsAsyncClientBuilder.AsyncBuilderParams.defaultExecutor] for more details.
 *
 * Example:
 *
 *   val client: AmazonEC2Async = buildClient()
 *   val describeResult: DescribeInstancesResult = suspendCommandAsync(client::describeInstancesAsync)
 */
@ExperimentalAwsCoroutineApi
suspend inline fun <AwsRequest : AmazonWebServiceRequest, AwsResult : AmazonWebServiceResult<*>> suspendCommandAsync(
    crossinline method: (handler: AsyncHandler<AwsRequest, AwsResult>) -> Future<AwsResult>
): AwsResult = suspendCancellableCoroutine {
    method(AwsContinuationAsyncHandler(it)).also { future ->
        it.cancelFutureOnCancellation(future)
    }
}

/**
 * Starts a [CancellableContinuation] that executes [method] and resumes with the [AwsResult].
 *
 * _Note_: This request is launched in the SDK client's executor threads.
 * See [com.amazonaws.client.builder.AwsAsyncClientBuilder.AsyncBuilderParams.defaultExecutor] for more details.
 *
 * Example:
 *
 *   val client: AmazonEC2Async = buildClient()
 *   val describeResult: DescribeImagesResult = suspendCommandAsync(client::describeImagesAsync) {
 *       DescribeImagesRequest()
 *          .withImageIds("ami-12312412431754", "ami-12334237461523")
 *   }
 */
@ExperimentalAwsCoroutineApi
suspend inline fun <AwsRequest : AmazonWebServiceRequest, AwsResult : AmazonWebServiceResult<*>> suspendCommandAsync(
    crossinline method: (request: AwsRequest, handler: AsyncHandler<AwsRequest, AwsResult>) -> Future<AwsResult>,
    crossinline requestBuilder: () -> AwsRequest
): AwsResult = suspendCancellableCoroutine {
    method(requestBuilder(), AwsContinuationAsyncHandler(it)).also { future ->
        it.cancelFutureOnCancellation(future)
    }
}

@InternalAwsCoroutineApi
class AwsContinuationAsyncHandler<AwsRequest : AmazonWebServiceRequest, AwsResult : AmazonWebServiceResult<*>>(
    cont: Continuation<AwsResult>
) : AsyncHandler<AwsRequest, AwsResult> {
    // Reference to continuation that can be cleared to assist with GC
    private var cont: Continuation<AwsResult>? = cont

    override fun onSuccess(request: AwsRequest, result: AwsResult) {
        cont?.resume(result)
        cont = null
    }

    override fun onError(exception: Exception) {
        cont?.resumeWithException(exception)
        cont = null
    }
}
