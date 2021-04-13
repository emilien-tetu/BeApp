package emilien.tetu.taptogo.domain.di

import emilien.tetu.taptogo.data.di.DataModule
import emilien.tetu.taptogo.domain.interactor.HomeInteractor
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModule {
    fun loadModules() {
        loadKoinModules(
            listOf(
                homeModule,
            )
        )
        DataModule.loadModules()
    }
}

val homeModule = module {
    factory { HomeInteractor(get(), get()) }
}