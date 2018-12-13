package com.github.bradynpoulsen.aws.coroutines

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.handlers.AsyncHandler
import kotlinx.coroutines.cancelFutureOnCancellation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Future
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Starts a cancellable coroutine that executes [method] and responds with the [AwsResult].
 *
 * Example:
 *
 *   val client: AmazonEC2Async = buildClient()
 *   val describeResult: DescribeInstancesResult = awsCoroutine(client::describeInstancesAsync)
 */
suspend inline fun <AwsRequest : AmazonWebServiceRequest, AwsResult : AmazonWebServiceResult<*>> awsCoroutine(
    crossinline method: (request: AwsRequest, handler: AsyncHandler<AwsRequest, AwsResult>) -> Future<AwsResult>,
    crossinline requestBuilder: () -> AwsRequest
): AwsResult = suspendCancellableCoroutine {
    method(requestBuilder(), AwsContinuationAsyncHandler(it)).also { future ->
        it.cancelFutureOnCancellation(future)
    }
}

/**
 * Starts a cancellable coroutine that executes [method] and responds with the [AwsResult].
 */
suspend inline fun <AwsRequest : AmazonWebServiceRequest, AwsResult : AmazonWebServiceResult<*>> awsCoroutine(
    crossinline method: (handler: AsyncHandler<AwsRequest, AwsResult>) -> Future<AwsResult>
): AwsResult = suspendCancellableCoroutine {
    method(AwsContinuationAsyncHandler(it)).also { future ->
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
