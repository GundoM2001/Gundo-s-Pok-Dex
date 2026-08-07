package com.example.pokedexapp.utils

import androidx.annotation.StringRes
import com.example.pokedexapp.R
import retrofit2.HttpException
import java.io.IOException

data class UiErrorMessage(
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int
)

object ErrorHandler {
    fun mapException(e: Throwable?): UiErrorMessage {
        return when (e) {
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
                    else -> UiErrorMessage(
                        titleRes = R.string.error_server_title,
                        messageRes = R.string.error_server_msg
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
