package net.marwanaziz.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import net.marwanaziz.cityscoutshared.SearchCityResult

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainView() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val scope = rememberCoroutineScope()
    var selectedCityName by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedCityCountry by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedCityLat by rememberSaveable { mutableStateOf<Double?>(null) }
    var selectedCityLon by rememberSaveable { mutableStateOf<Double?>(null) }
    val selectedCity =
        if (selectedCityName != null && selectedCityCountry != null && selectedCityLat != null && selectedCityLon != null) {
            SearchCityResult(
                name = selectedCityName!!,
                country = selectedCityCountry!!,
                lat = selectedCityLat!!,
                lon = selectedCityLon!!
            )
        } else {
            null
        }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
        selectedCityName = null
        selectedCityCountry = null
        selectedCityLat = null
        selectedCityLon = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ListDetailPaneScaffold(
            scaffoldState = navigator.scaffoldState,
            listPane = {
                SearchCityView(
                    onCitySelectedListener = {
                        selectedCityName = it.name
                        selectedCityCountry = it.country
                        selectedCityLat = it.lat
                        selectedCityLon = it.lon
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
