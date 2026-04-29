package net.marwanaziz.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.marwanaziz.cityscoutshared.CityWeatherViewModel
import javax.inject.Inject


@HiltViewModel
class CityWeatherHiltViewModel @Inject constructor(
    val cityWeatherViewModel: CityWeatherViewModel
) : ViewModel()