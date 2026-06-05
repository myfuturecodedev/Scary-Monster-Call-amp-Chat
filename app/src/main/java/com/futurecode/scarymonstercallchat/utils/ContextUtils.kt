package com.futurecode.scarymonstercallchat.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object ContextUtils {
    fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }
}