package emilien.tetu.taptogo.data.api

import android.util.Log
import retrofit2.Response

interface ApiWrapper {
    fun <T : Any> manageResponse(response: Response<T>): ApiResponse<T> = with(response) {
        Log.d("DEBUG","response: $response")
        when (isSuccessful) {
            true -> {
                body()?.let {
                    Log.d("DEBUG","response = $it")
                    return ApiResponse.Success(it)
                } ?: return ApiResponse.Error(ApiError.NO_DATA)
            }
            false -> {
                errorBody()?.let {
                    Log.d("DEBUG","${it.string()} : ${code()}")
                    return ApiResponse.ErrorMessage(it.string(), code())
                } ?: return ApiResponse.Error(ApiError.NO_DATA)
            }
        }
    }

    enum class ApiError {
        NO_DATA
    }
}
