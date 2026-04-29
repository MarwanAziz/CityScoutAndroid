package net.marwanaziz.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import net.marwanaziz.cityscoutshared.CityWeatherViewModel
import net.marwanaziz.cityscoutshared.SearchCityResult
import net.marwanaziz.cityscoutshared.WeatherForecastViewModel

@Composable
fun CityWeatherView(
    viewModel: CityWeatherViewModel = hiltViewModel<CityWeatherHiltViewModel>().cityWeatherViewModel,
    city: SearchCityResult
) {
    val isLoading = viewModel.loading.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(city) {
        viewModel.checkWeather(city)
    }
    Scaffold(
        topBar = { ViewTopAppBar(city) },
        containerColor = Color.Transparent
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
                .padding(horizontal = 0.dp)
                .verticalScroll(scrollState)
        ) {
            if (isLoading.value) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                CurrentWeather(viewModel)
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp), thickness = 0.5.dp)
                ForecastView(viewModel)
                TempToggleView(viewModel)
            }
        }
    }
}

@Composable
private  fun TempToggleView(viewModel: CityWeatherViewModel) {
    val options = listOf(stringResource(R.string.celsius), stringResource(R.string.fahrenheit))
    val isCelsius = viewModel.isCelsius.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SingleChoiceSegmentedButtonRow() {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = if (index == 0) isCelsius.value else !isCelsius.value,
                    onClick = {
                        viewModel.toggleTemperatureFormat()
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index,
                        options.size
                    )
                ) {
                    Text(label)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewTopAppBar(city: SearchCityResult) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        title = {
            Column() {
                Text(
                    city.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    city.country,
                    fontWeight = FontWeight.Thin,
                    fontSize = 15.sp
                )
            }
        },
    )
}

@Composable
private fun CurrentWeather(viewModel: CityWeatherViewModel) {
    val temp = viewModel.weatherTemp.collectAsState()
    val feelsLike = viewModel.weatherFeelsLike.collectAsState()
    val condition = viewModel.weatherConditionText.collectAsState()
    val humidity = viewModel.weatherHumidity.collectAsState()
    val weatherConditionIcon = viewModel.weatherConditionIcon.collectAsState()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = validateUrlProtocol(weatherConditionIcon.value ?: "")
        AsyncMedia(imageUrl)
        Text(
            temp.value,
            fontWeight = FontWeight.Black,
            fontSize = 37.sp
        )
        Text(
            condition.value,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
        Text(
            feelsLike.value,
            modifier = Modifier.padding(vertical = 16.dp),
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherStat(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.WaterDrop,
                label = stringResource(R.string.humidity),
                value = humidity.value
            )
            WeatherStat(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Air,
                label = stringResource(R.string.wind),
                value = viewModel.weatherWindSpeed.collectAsState().value
            )
            WeatherStat(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Visibility,
                label = stringResource(R.string.visibility),
                value = viewModel.weatherVisibility.collectAsState().value
            )
        }
    }
}

private fun validateUrlProtocol(url: String): String {
    if (url.startsWith("https:")) {
        return url
    }
    return "https:$url"
}

@Composable
private fun AsyncMedia(imageUrl: String, size: Dp = 64.dp, ) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier.size(size),
        contentScale = ContentScale.Fit
    ) {
        when (val state = painter.state) {
            else -> SubcomposeAsyncImageContent()
        }
    }
}

@Composable
private fun WeatherStat(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 0.2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 5.dp,
                    topEnd = 5.dp,
                    bottomEnd = 5.dp,
                    bottomStart = 5.dp
                ),
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                label.uppercase(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                value,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ForecastView(viewModel: CityWeatherViewModel) {
    val forecast = viewModel.forecasts.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp)
    ) {
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        Text(
            stringResource(R.string.day_forecast, forecast.value.size),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.padding(bottom = 8.dp))

        Row(
            modifier = Modifier.horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (day in forecast.value) {
                ForecastDayView(day)
            }
        }
    }
}

@Composable
private fun ForecastDayView(viewModel: WeatherForecastViewModel) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .border(
                0.2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 5.dp,
                    topEnd = 5.dp,
                    bottomEnd = 5.dp,
                    bottomStart = 5.dp
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val spaceSize: Dp = 4.dp
            Text(viewModel.dayOfWeek)
            AsyncMedia(
                validateUrlProtocol(viewModel.weatherConditionIcon ?: ""),
                30.dp
            )
            Spacer(modifier = Modifier.padding(vertical = spaceSize))
            Text(viewModel.weatherConditionText)
            Spacer(modifier = Modifier.padding(vertical = spaceSize))

            Row(
                horizontalArrangement = Arrangement.Absolute.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    viewModel.weatherMaxTemp,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
                Text(
                    viewModel.weatherMinTemp,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.padding(vertical = spaceSize))
            Text("💧${viewModel.weatherHumidity}")
        }
    }
}

