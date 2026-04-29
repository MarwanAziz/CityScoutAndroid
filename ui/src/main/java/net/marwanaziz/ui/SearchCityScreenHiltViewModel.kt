package net.marwanaziz.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.marwanaziz.cityscoutshared.CityWeatherViewModel
import javax.inject.Inject
import net.marwanaziz.cityscoutshared.SearchCityViewModel

@HiltViewModel
class SearchCityScreenHiltViewModel @Inject constructor(
    val searchViewModel: SearchCityViewModel
) : ViewModel()

@HiltViewModel
class CityWeatherHiltViewModel @Inject constructor(
    val cityWeatherViewModel: CityWeatherViewModel
) : ViewModel()
