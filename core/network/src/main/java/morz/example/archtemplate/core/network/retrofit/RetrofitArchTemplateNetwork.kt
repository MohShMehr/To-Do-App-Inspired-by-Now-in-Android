package morz.example.archtemplate.core.network.retrofit

import com.squareup.moshi.JsonClass

internal const val ARCH_TEMPLATE_BASE_URL = "https://dummyjson.com/"

@JsonClass(generateAdapter = true)
data class NetworkResponse<T>(
    val data: T
)