package net.marwanaziz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        ListDetailPaneScaffold (
            listPane = { SearchCityScreenView() },
            detailPane = { CityWeatherScreenView() }
        )
    }
}

@Composable
fun CityWeatherScreenView() {
    Text("Hello Details view!")
}


@Preview(
    name = "PIXEL_C",
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
fun MainViewPreview() {
    MainView()
}
