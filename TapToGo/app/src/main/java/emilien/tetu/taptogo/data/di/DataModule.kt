package emilien.tetu.taptogo.data.di

import emilien.tetu.taptogo.data.repository.HomeRepository
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModule {
    fun loadModules() {
        loadKoinModules(
            listOf(
                homeDataModule,
            )
        )
    }
}

val homeDataModule = module {
    single{ HomeRepository() }
}