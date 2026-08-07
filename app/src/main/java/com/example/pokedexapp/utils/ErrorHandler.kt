package com.example.pokedexapp.utils

import androidx.annotation.StringRes
import com.example.pokedexapp.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class UiErrorMessage(
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int
)

object ErrorHandler {
    fun mapException(e: Throwable?): UiErrorMessage {
        return when (e) {
            is UnknownHostException -> UiErrorMessage(
                titleRes = R.string.error_network_title,
                messageRes = R.string.error_network_msg
            )
            is SocketTimeoutException -> UiErrorMessage(
                titleRes = R.string.error_timeout_title,
                messageRes = R.string.error_timeout_msg
            )
            is java.net.ConnectException -> UiErrorMessage(
                titleRes = R.string.error_network_title,
                messageRes = R.string.error_network_msg
            )
            is com.google.gson.JsonSyntaxException -> UiErrorMessage(
                titleRes = R.string.error_data_title,
                messageRes = R.string.error_data_msg
            )
            is IOException -> UiErrorMessage(
                titleRes = R.string.error_network_title,
                messageRes = R.string.error_network_msg
            )
            is HttpException -> {
                when (e.code()) {
                    404 -> UiErrorMessage(
                        titleRes = R.string.error_not_found_title,
                        messageRes = R.string.error_not_found_msg
                    )
                    500, 502, 503, 504 -> UiErrorMessage(
                        titleRes = R.string.error_server_title,
                        messageRes = R.string.error_server_msg
                    )
                    else -> UiErrorMessage(
                        titleRes = R.string.error_unknown_title,
                        messageRes = R.string.unknown_error
                    )
                }
            }
            else -> UiErrorMessage(
                titleRes = R.string.error_unknown_title,
                messageRes = R.string.unknown_error
            )
        }
    }
}
