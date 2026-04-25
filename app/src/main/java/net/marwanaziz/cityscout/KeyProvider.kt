package net.marwanaziz.cityscout

object KeyProvider {
    val rapidApiKey: String
        get() = BuildConfig.RAPID_API_KEY

    val weatherApiKey: String
        get() = BuildConfig.WEATHER_API_KEY
}
