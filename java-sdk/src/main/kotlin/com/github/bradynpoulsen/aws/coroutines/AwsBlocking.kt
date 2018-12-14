package com.github.bradynpoulsen.aws.coroutines

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Executes the blocking [method] in the [context] (defaults to [Dispatchers.IO]).
 */
@ExperimentalAwsCoroutineApi
suspend inline fun <AwsRequest : AmazonWebServiceRequest, AwsResult : AmazonWebServiceResult<*>> awsBlockingCoroutine(
    crossinline method: (request: AwsRequest) -> AwsResult,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline requestBuilder: () -> AwsRequest
): AwsResult = withContext(Dispatchers.IO + context) {
    method(requestBuilder())
}

/**
 * Executes the blocking [method] in the [context] (defaults to [Dispatchers.IO]).
 */
@ExperimentalAwsCoroutineApi
suspend inline fun <AwsResult : AmazonWebServiceResult<*>> awsBlockingCoroutine(
    crossinline method: () -> AwsResult,
    context: CoroutineContext = EmptyCoroutineContext
): AwsResult = withContext(Dispatchers.IO + context) {
    method()
}
