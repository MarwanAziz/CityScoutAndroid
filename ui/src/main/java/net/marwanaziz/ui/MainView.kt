package net.marwanaziz.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val scope = rememberCoroutineScope()
    var selectedCity by rememberSaveable(stateSaver = selectedCitySaver) { mutableStateOf<SearchCityResult?>(null) }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
        selectedCity = null
    }

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
                        selectedCity = it
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        }
                    }
                )
            },
            detailPane = {
                selectedCity?.let { city ->
                    Box {
                        CityWeatherView(city = city)
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

private val selectedCitySaver = Saver<SearchCityResult?, List<Any>>(
    save = { city ->
        city?.let { listOf(it.name, it.country, it.lat, it.lon) } ?: emptyList()
    },
    restore = { data ->
        if (data.isEmpty()) {
            null
        } else {
            SearchCityResult(
                name = data[0] as String,
                country = data[1] as String,
                lat = data[2] as Double,
                lon = data[3] as Double
            )
        }
    }
)
