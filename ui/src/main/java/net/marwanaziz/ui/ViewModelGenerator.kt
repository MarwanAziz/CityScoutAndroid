package net.marwanaziz.ui
import net.marwanaziz.cityscoutshared.CityScoutFactory
import net.marwanaziz.cityscoutshared.Remote
import net.marwanaziz.cityscoutshared.RemoteKeys

internal object ViewModelGenerator {

    private val remote: Remote by lazy {
        if (UIApiKeys.rapidApiKey.isEmpty() || UIApiKeys.weatherApiKey.isEmpty()) {
            error("API keys are missing")
        }
        RemoteKeys.rapidApiKey = UIApiKeys.rapidApiKey
        RemoteKeys.weatherApiKey = UIApiKeys.weatherApiKey
        CityScoutFactory.createRemote(RemoteKeys)
    }

    val searchViewModel = CityScoutFactory.creatSearchCityViewModel(remote)
    val weatherViewModel = CityScoutFactory.createWeatherViewModel(remote)
}