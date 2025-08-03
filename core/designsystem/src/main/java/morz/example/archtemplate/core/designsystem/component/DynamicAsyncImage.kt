package morz.example.archtemplate.core.designsystem.component

import morz.example.archtemplate.core.designsystem.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter

@Composable
fun DynamicAsyncImage(
    imageUrl: String? = null,
    @DrawableRes imageDrawable: Int? = null,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    placeholder: Painter = painterResource(R.drawable.ic_avatar),
    contentScale: ContentScale = ContentScale.Fit
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is Loading
            isError = state is Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading && !isLocalInspection) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        val painter = if (imageDrawable != null)
            painterResource(imageDrawable)
        else if (isError.not() && !isLocalInspection)
            imageLoader
        else
            placeholder

        Image(
            contentScale = contentScale,
            painter = painter,
            contentDescription = contentDescription
        )
    }
}

