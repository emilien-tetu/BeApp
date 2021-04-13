package emilien.tetu.taptogo.koin

import emilien.tetu.taptogo.presentation.di.PresentationModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object AppModule {
    fun loadModules() {
        loadKoinModules(listOf(loginAppModule))
        PresentationModule.loadModules()
    }
}

val loginAppModule = module {
}

