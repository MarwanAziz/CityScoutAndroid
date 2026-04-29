package net.marwanaziz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import net.marwanaziz.cityscoutshared.SearchCityResult

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainView() {
    val navigator = rememberListDetailPaneScaffoldNavigator<SearchCityResult>()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        ListDetailPaneScaffold(
            scaffoldState = navigator.scaffoldState,
            listPane = {
                SearchCityView(
                    onCitySelectedListener = {
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                        }
                    }
                )
            },
            detailPane = {
                val selectedCity = navigator.currentDestination?.content
                if (selectedCity != null) {
                    Box {
                        CityWeatherView(city = selectedCity)
                    }
                }
            }
        )
    }
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
