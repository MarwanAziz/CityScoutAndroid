package net.marwanaziz.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.marwanaziz.cityscoutshared.SearchCityResult
import net.marwanaziz.cityscoutshared.SearchCityViewModel

@Composable
@Preview
fun SearchCityViewPreview() {
    SearchCityView(FakeSearchViewModel()) { selectedCity ->
        print("Selected city ${selectedCity.name}")
    }
}

private class FakeSearchViewModel : SearchCityViewModel {
    override val searchCityResult: StateFlow<List<SearchCityResult>>
    override val searchError: StateFlow<String>
    override val loading: StateFlow<Boolean>
    private var _searchCityResult = MutableStateFlow<List<SearchCityResult>>(emptyList())
    private var _searchError: MutableStateFlow<String> = MutableStateFlow("")
    private var _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    constructor() {
        searchCityResult = _searchCityResult
        searchError = _searchError
        loading = _loading
    }

    override suspend fun searchCity(city: String) {
        if (city.count() < 2) {
            _searchError.value = "Preview error test"
            _searchCityResult.value = emptyList()
            return
        }

        _searchError.value = ""
        _searchCityResult.value = listOf(
            SearchCityResult(
                "London",
                "United Kingdom",
                0.0,
                0.0
            ),
            SearchCityResult(
                "Paris",
                "France",
                0.0,
                0.0
            ),
            SearchCityResult(
                "Rome",
                "Italy",
                0.0,
                0.0
            )
        )
    }
}
