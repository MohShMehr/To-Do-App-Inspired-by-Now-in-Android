package morz.example.archtemplate.core.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Home(
    val message: String,
    val status: String
)