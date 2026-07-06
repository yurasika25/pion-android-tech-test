package com.example.sbtechnicaltest.core.network

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Executes API work and converts common network failures into user-readable [Result] failures.
 *
 * Coroutine cancellation is rethrown. HTTP and unknown failures use the repository-specific
 * [fallbackMessage], while timeout, offline, and network failures use shared messages.
 */
internal suspend fun <T> safeApiCall(
    fallbackMessage: String = "Something went wrong. Please try again.",
    block: suspend () -> T,
): Result<T> = try {
    Result.success(block())
} catch (exception: CancellationException) {
    throw exception
} catch (exception: SocketTimeoutException) {
    Result.failure(Exception("Request timed out. Please try again.", exception))
} catch (exception: UnknownHostException) {
    Result.failure(
        Exception(
            "No internet connection. Please check your connection.",
            exception,
        ),
    )
} catch (exception: IOException) {
    Result.failure(Exception("Network error. Please try again.", exception))
} catch (exception: HttpException) {
    Result.failure(Exception(fallbackMessage, exception))
} catch (exception: Exception) {
    Result.failure(Exception(fallbackMessage, exception))
}
