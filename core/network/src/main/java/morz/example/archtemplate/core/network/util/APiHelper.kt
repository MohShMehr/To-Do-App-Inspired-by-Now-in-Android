package morz.example.archtemplate.core.network.util

import retrofit2.HttpException
import retrofit2.Response

sealed class ApiResult<out T> {
    data class Success<T>(val data: T): ApiResult<T>()
    data class Error(val throwable: Throwable): ApiResult<Nothing>()
}

object APiHelper {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error(HttpException(response))
            }
        } catch (e: Throwable) {
            ApiResult.Error(e)
        }
    }

}