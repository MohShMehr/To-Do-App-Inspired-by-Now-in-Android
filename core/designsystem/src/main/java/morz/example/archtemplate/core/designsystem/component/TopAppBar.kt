package morz.example.archtemplate.core.designsystem.component

import morz.example.archtemplate.core.designsystem.R
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    @DrawableRes titleImage: Int = R.drawable.ic_avatar,
    @DrawableRes navigationIcon: Int,
    navigationIconContentDescription: String?,
    @DrawableRes actionIcon: Int,
    actionIconContentDescription: String?,
    modifier: Modifier = Modifier,
    @ColorRes bgColor: Int = R.color.dark_gray,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                DynamicAsyncImage(
                    imageDrawable = titleImage,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .height(40.dp)
                )
            }
        },
        navigationIcon = {

            IconButton(onClick = onNavigationClick) {
                DynamicAsyncImage(
                    imageDrawable = navigationIcon,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp),
                    contentDescription = navigationIconContentDescription
                )
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                DynamicAsyncImage(
                    imageDrawable = actionIcon,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp),
                    contentDescription = actionIconContentDescription
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = bgColor)
        ),
        modifier = modifier.testTag("AppTopAppBar"),
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun TopAppBarPreview() {
    AppTopAppBar(
        navigationIcon = R.drawable.ic_avatar,
        navigationIconContentDescription = "App TopAppBar navigation icon bank login",
        actionIcon = R.drawable.ic_avatar,
        actionIconContentDescription = "App TopAppBar action icon avatar"
    )
}