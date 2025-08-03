package morz.example.archtemplate.feature.work

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun WorkRoute(
    modifier: Modifier = Modifier,
    viewModel: WorkViewModel = hiltViewModel()
) {
    WorkScreen()
}

@Composable
internal fun WorkScreen(
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Work")
    }

}