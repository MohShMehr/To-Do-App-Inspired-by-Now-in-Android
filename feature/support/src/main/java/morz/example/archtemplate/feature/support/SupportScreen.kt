package morz.example.archtemplate.feature.support

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun SupportRoute(
    modifier: Modifier = Modifier,
    viewModel: SupportViewModel = hiltViewModel()
) {
    SupportScreen()
}

@Composable
internal fun SupportScreen(
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Support")
    }

}