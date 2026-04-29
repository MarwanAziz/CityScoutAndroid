package net.marwanaziz.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.marwanaziz.cityscoutshared.CityWeatherViewModel
import net.marwanaziz.cityscoutshared.CurrentWeather
import net.marwanaziz.cityscoutshared.SearchCityResult
import net.marwanaziz.cityscoutshared.WeatherAndForecast
import net.marwanaziz.cityscoutshared.WeatherCondition
import net.marwanaziz.cityscoutshared.WeatherDay
import net.marwanaziz.cityscoutshared.WeatherForecast
import net.marwanaziz.cityscoutshared.WeatherForecastViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CityWeatherViewPreview() {
    val viewModel = FakeCityWeatherViewModel()
    val searchResult = SearchCityResult(
        "London",
        "United Kingdom",
        51.5074,
        -0.1278
    )

    MaterialTheme {
        CityWeatherView(viewModel = viewModel, city = searchResult)
    }
}

private class FakeCityWeatherViewModel : CityWeatherViewModel {
    override val title: StateFlow<String> = MutableStateFlow("London")
    override val country: StateFlow<String> = MutableStateFlow("United Kingdom")
    override val weatherConditionText: StateFlow<String> = MutableStateFlow("Partly cloudy")
    override val weatherConditionIcon: StateFlow<String> = MutableStateFlow("https://cdn.weatherapi.com/weather/64x64/day/113.png")
    override val weatherConditionCode: StateFlow<Int> = MutableStateFlow(1003)
    override val weatherTemp: StateFlow<String> = MutableStateFlow("18 C")
    override val weatherHumidity: StateFlow<String> = MutableStateFlow("62%")
    override val weatherWindSpeed: StateFlow<String> = MutableStateFlow("14 km/h")
    override val weatherFeelsLike: StateFlow<String> = MutableStateFlow("Feels like 17 C")
    override val weatherVisibility: StateFlow<String> = MutableStateFlow("10 km")
    override val forecasts: StateFlow<List<WeatherForecastViewModel>> = MutableStateFlow(
        listOf(
            FakeWeatherForecastViewModel("Mon", "22 C", "13 C", "60%", "11 km/h", "Sunny"),
            FakeWeatherForecastViewModel("Tue", "20 C", "12 C", "64%", "14 km/h", "Cloudy"),
            FakeWeatherForecastViewModel("Wed", "18 C", "11 C", "70%", "17 km/h", "Rain"),
            FakeWeatherForecastViewModel("Thu", "19 C", "10 C", "58%", "13 km/h", "Windy"),
            FakeWeatherForecastViewModel("Fri", "21 C", "12 C", "55%", "10 km/h", "Clear")
        )
    )
    override val weatherError: StateFlow<String> = MutableStateFlow("")
    override val loading: StateFlow<Boolean> = MutableStateFlow(false)
    override val isCelsius: StateFlow<Boolean> = MutableStateFlow(true)

    override suspend fun checkWeather(city: SearchCityResult) = Unit

    override fun toggleTemperatureFormat() = Unit
}

private class FakeWeatherForecastViewModel(
    private val dayOfWeekValue: String,
    private val maxTempValue: String,
    private val minTempValue: String,
    private val humidityValue: String,
    private val windSpeedValue: String,
    private val conditionTextValue: String
) : WeatherForecastViewModel {
    override val weatherForecast: WeatherAndForecast = WeatherAndForecast(
        CurrentWeather(
            WeatherCondition(conditionTextValue, "https://cdn.weatherapi.com/weather/64x64/day/113.png", 1000),
            20.0,
            68.0,
            60,
            7.0,
            11.0,
            "NW",
            19.0,
            66.0,
            6.0,
            10.0,
            1,
            20
        ),
        WeatherForecast(
            listOf(
                WeatherDay(
                    1,
                    22.0,
                    71.6,
                    13.0,
                    55.4,
                    60,
                    7.0,
                    11.0,
                    "NW",
                    WeatherCondition(conditionTextValue, "https://cdn.weatherapi.com/weather/64x64/day/113.png", 1000)
                )
            )
        )
    )
    override val weatherConditionText: String = conditionTextValue
    override val weatherConditionIcon: String = "https://cdn.weatherapi.com/weather/64x64/day/113.png"
    override val weatherMaxTemp: String = maxTempValue
    override val weatherMinTemp: String = minTempValue
    override val weatherHumidity: String = humidityValue
    override val weatherWindSpeed: String = windSpeedValue
    override val dayOfWeek: String = dayOfWeekValue
}
