package emilien.tetu.taptogo.data.api

sealed class ApiResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResponse<T>()
    data class ErrorMessage(val errorMessage: String, val code: Int = 0) : ApiResponse<Nothing>()
    data class Error(val errorMessage: ApiWrapper.ApiError) : ApiResponse<Nothing>()
}